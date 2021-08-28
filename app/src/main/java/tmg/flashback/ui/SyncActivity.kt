package tmg.flashback.ui

import android.content.Intent
import android.os.Bundle
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.common.ui.forceupgrade.ForceUpgradeActivity
import tmg.core.ui.base.BaseActivity
import tmg.flashback.databinding.ActivitySyncBinding
import tmg.flashback.ui.dashboard.HomeActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.views.show

class SyncActivity: BaseActivity() {

    private val viewModel: SyncViewModel by viewModel()

    private lateinit var binding: ActivitySyncBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySyncBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observe(viewModel.outputs.showLoading) {
            binding.splash.show(it)
        }

        observe(viewModel.outputs.showResync) {
            binding.smiley.show(it)
            binding.tryAgain.show(it)
            binding.failedToSync.show(it)
        }

        observeEvent(viewModel.outputs.goToDashboard) {
            startActivity(Intent(this@SyncActivity, HomeActivity::class.java))
            finish()
        }

        observeEvent(viewModel.outputs.goToForceUpgrade) {
            startActivity(Intent(this@SyncActivity, ForceUpgradeActivity::class.java))
            finish()
        }

        binding.tryAgain.setOnClickListener {
            viewModel.inputs.start()
        }

        viewModel.inputs.start()
    }
}
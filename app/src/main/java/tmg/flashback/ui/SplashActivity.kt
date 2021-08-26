package tmg.flashback.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.common.ui.forceupgrade.ForceUpgradeActivity
import tmg.flashback.databinding.ActivitySplashBinding
import tmg.flashback.ui.dashboard.DashboardActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.views.show

class SplashActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
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
            startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
            finish()
        }

        observeEvent(viewModel.outputs.goToForceUpgrade) {
            startActivity(Intent(this@SplashActivity, ForceUpgradeActivity::class.java))
            finish()
        }

        binding.tryAgain.setOnClickListener {
            viewModel.inputs.start()
        }

        viewModel.inputs.start()
    }
}
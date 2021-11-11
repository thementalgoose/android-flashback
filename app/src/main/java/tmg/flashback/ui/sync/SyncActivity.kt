package tmg.flashback.ui.sync

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.common.ui.forceupgrade.ForceUpgradeActivity
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.R
import tmg.flashback.databinding.ActivitySyncBinding
import tmg.flashback.databinding.ViewSyncItemBinding
import tmg.flashback.ui.dashboard.HomeActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.setStatusBarColor
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.invisible
import tmg.utilities.extensions.views.visible

class SyncActivity: BaseActivity() {

    private val viewModel: SyncViewModel by viewModel()

    private lateinit var binding: ActivitySyncBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySyncBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBarColor(ContextCompat.getColor(this, R.color.splash_screen))

        binding.tryAgain.setOnClickListener {
            viewModel.inputs.startLoading()
        }

        observe(viewModel.outputs.loadingState) {
            when (it) {
                SyncState.LOADING -> {
                    binding.progress.visible()
                    binding.tryAgain.invisible()
                }
                SyncState.DONE -> binding.progress.visible()
                SyncState.FAILED -> {
                    binding.progress.invisible()
                    binding.tryAgain.visible()
                }
            }
        }

        observe(viewModel.outputs.showRetry) {
            when (it) {
                true -> {
                    binding.failedToSync.visible()
                    binding.tryAgain.visible()
                }
                false -> {
                    binding.failedToSync.gone()
                    binding.tryAgain.invisible()
                }
            }
        }

        observeEvent(viewModel.outputs.navigate) {
            when (it) {
                SyncNavTarget.DASHBOARD -> {
                    startActivity(Intent(this@SyncActivity, HomeActivity::class.java))
                    finish()
                }
                SyncNavTarget.FORCE_UPGRADE -> {
                    startActivity(Intent(this@SyncActivity, ForceUpgradeActivity::class.java))
                    finish()
                }
            }
        }

        viewModel.inputs.startLoading()
    }

    private fun ViewSyncItemBinding.updateTo(state: SyncState) {
        when (state) {
            SyncState.LOADING -> {
                this.container.isClickable = false
                this.container.isEnabled = false
                this.loading.visible()
                this.done.invisible()
                this.failed.invisible()
            }
            SyncState.DONE -> {
                this.container.isClickable = false
                this.container.isEnabled = false
                this.loading.invisible()
                this.done.visible()
                this.failed.invisible()
            }
            SyncState.FAILED -> {
                this.container.isClickable = true
                this.container.isEnabled = true
                this.loading.invisible()
                this.done.invisible()
                this.failed.visible()
            }
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }
}
package tmg.flashback.ui.sync

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.R
import tmg.flashback.forceupgrade.ForceUpgradeNavigationComponent
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.HomeActivity
import tmg.flashback.ui.base.BaseActivity
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.setStatusBarColor
import javax.inject.Inject

@AndroidEntryPoint
class SyncActivity: BaseActivity() {

    private val viewModel: SyncViewModel by viewModels()

    @Inject
    protected lateinit var forceUpgradeNavigationComponent: ForceUpgradeNavigationComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Sync")

        setContent {
            AppTheme {
                Scaffold(content = {
                    val showRetry = viewModel.outputs.showRetry.observeAsState(false)

                    SyncScreen(
                        showLoading = !showRetry.value,
                        tryAgainClicked = viewModel.inputs::startLoading
                    )
                })
            }
        }

        setStatusBarColor(ContextCompat.getColor(this, R.color.splash_screen))

        observeEvent(viewModel.outputs.navigate) {
            when (it) {
                SyncNavTarget.DASHBOARD -> {
                    startActivity(Intent(this@SyncActivity, HomeActivity::class.java))
                    finish()
                }
                SyncNavTarget.FORCE_UPGRADE -> {
                    forceUpgradeNavigationComponent.forceUpgrade()
                    finish()
                }
            }
        }

        viewModel.inputs.startLoading()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }
}
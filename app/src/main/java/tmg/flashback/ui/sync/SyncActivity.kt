package tmg.flashback.ui.sync

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.content.ContextCompat
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.common.CommonNavigationComponent
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.ui.HomeActivity
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.setStatusBarColor

class SyncActivity: BaseActivity() {

    private val viewModel: SyncViewModel by viewModel()
    private val commonNavigationComponent: CommonNavigationComponent by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                    commonNavigationComponent.forceUpgradeLaunch()
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
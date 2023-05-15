package tmg.flashback.ui.sync

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.R
import tmg.flashback.maintenance.contract.MaintenanceNavigationComponent
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.HomeActivity
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.ui.sync.SyncState.LOADING
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.setStatusBarColor
import javax.inject.Inject

@AndroidEntryPoint
class SyncActivity: BaseActivity() {

    private val viewModel: SyncViewModel by viewModels()

    @Inject
    protected lateinit var maintenanceNavigationComponent: MaintenanceNavigationComponent

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Sync")

        setContent {
            AppTheme {
                Scaffold(content = {
                    val drivers = viewModel.outputs.driversState.observeAsState(initial = LOADING)
                    val circuits = viewModel.outputs.circuitsState.observeAsState(initial = LOADING)
                    val constructors = viewModel.outputs.constructorsState.observeAsState(initial = LOADING)
                    val races = viewModel.outputs.racesState.observeAsState(initial = LOADING)
                    val config = viewModel.outputs.configState.observeAsState(initial = LOADING)
                    val showTryAgain = viewModel.outputs.showRetry.observeAsState(false)

                    SyncScreen(
                        drivers = drivers.value,
                        circuits = circuits.value,
                        config = config.value,
                        constructors = constructors.value,
                        races = races.value,
                        showTryAgain = showTryAgain.value,
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
                    maintenanceNavigationComponent.forceUpgrade()
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
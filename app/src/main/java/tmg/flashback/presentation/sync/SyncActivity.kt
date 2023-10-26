package tmg.flashback.presentation.sync

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tmg.flashback.R
import tmg.flashback.maintenance.contract.MaintenanceNavigationComponent
import tmg.flashback.style.AppTheme
import tmg.flashback.presentation.HomeActivity
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.presentation.sync.SyncState.LOADING
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

        setContent {
            AppTheme {
                Scaffold(content = {
                    val drivers = viewModel.outputs.driversState.collectAsState(initial = LOADING)
                    val circuits = viewModel.outputs.circuitsState.collectAsState(initial = LOADING)
                    val constructors = viewModel.outputs.constructorsState.collectAsState(initial = LOADING)
                    val races = viewModel.outputs.racesState.collectAsState(initial = LOADING)
                    val config = viewModel.outputs.configState.collectAsState(initial = LOADING)
                    val showTryAgain = viewModel.outputs.showRetry.collectAsState(false)

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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.outputs.navigate.collect {
                    when (it) {
                        SyncNavTarget.DASHBOARD -> {
                            startActivity(Intent(this@SyncActivity, HomeActivity::class.java))
                            finish()
                        }
                        SyncNavTarget.FORCE_UPGRADE -> {
                            maintenanceNavigationComponent.forceUpgrade()
                            finish()
                        }
                        else -> { /* Do nothing */ }
                    }
                }
            }
        }

        viewModel.inputs.startLoading()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }
}
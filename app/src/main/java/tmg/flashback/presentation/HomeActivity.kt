package tmg.flashback.presentation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowLayoutInfo
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.BuildConfig
import tmg.flashback.ads.ads.components.AdvertProvider
import tmg.flashback.configuration.usecases.ConfigSyncUseCase
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.maintenance.contract.MaintenanceNavigationComponent
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.constants.SCREEN_PARAM
import tmg.flashback.presentation.dashboard.DashboardScreen
import tmg.flashback.presentation.sync.SyncActivity
import tmg.flashback.season.usecases.ContentSyncUseCase
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity
import javax.inject.Inject

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class HomeActivity: BaseActivity(), SplashScreen.KeepOnScreenCondition {

    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var contentSyncUseCase: ContentSyncUseCase
    @Inject
    lateinit var configSyncUseCase: ConfigSyncUseCase
    @Inject
    lateinit var crashController: CrashlyticsManager
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var maintenanceNavigationComponent: MaintenanceNavigationComponent

    @Inject
    lateinit var advertProvider: AdvertProvider

    private var deeplink: String? = null

    override fun onSaveInstanceState(outState: Bundle) {
        outState.remove(SCREEN_PARAM)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        deeplink = savedInstanceState.getString(SCREEN_PARAM)
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (deeplink == null) {
            deeplink = intent.extras?.getString(SCREEN_PARAM)
        }

        setTheme(themeRes)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setSplashScreenTheme(themeRes)
        }
        val splashScreen = installSplashScreen()
        setTheme(themeRes)

        val windowInfoTracker = WindowInfoTracker.getOrCreate(this)
            .windowLayoutInfo(this@HomeActivity)

        setContent {
            AppTheme {
                DashboardScreen(
                    windowSize = calculateWindowSizeClass(activity = this),
                    windowLayoutInfo = windowInfoTracker.collectAsState(WindowLayoutInfo(emptyList())).value,
                    navigator = navigator,
                    advertProvider = advertProvider,
                    closeApp = { finish() },
                    viewModelStore = this.viewModelStore,
                    deeplink = deeplink
                )
            }
        }

        splashScreen.setKeepOnScreenCondition(this)

        viewModel.initialise()

        when {
            viewModel.requiresSync -> {
                startActivity(Intent(this@HomeActivity, SyncActivity::class.java))
                finish()
            }
            viewModel.forceUpgrade -> {
                maintenanceNavigationComponent.forceUpgrade()
                finish()
            }
        }

        // Content sync
        try {
            contentSyncUseCase.schedule()
            configSyncUseCase.schedule()
        } catch (e: Exception) {
            crashController.logException(e, "Failed to synchronise content when reaching home screen")
            if (BuildConfig.DEBUG) {
                Log.e("Home", "Failed to schedule content sync due to unknown exception")
                e.printStackTrace()
            }
        }

        viewModel.loadAppReview()
    }

    override fun onStop() {
        super.onStop()
        viewModel.cancelAppReview()
    }

    override fun shouldKeepOnScreen(): Boolean = viewModel.appliedChanges
}
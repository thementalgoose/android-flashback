package tmg.flashback.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.BuildConfig
import tmg.flashback.configuration.usecases.ConfigSyncUseCase
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.forceupgrade.ForceUpgradeNavigationComponent
import tmg.flashback.rss.RSS
import tmg.flashback.stats.Search
import tmg.flashback.stats.usecases.ContentSyncUseCase
import tmg.flashback.style.AppTheme
import tmg.flashback.style.utils.rememberWindowSizeClass
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import tmg.flashback.ui.sync.SyncActivity
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity: BaseActivity(), SplashScreen.KeepOnScreenCondition {

    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var contentSyncUseCase: ContentSyncUseCase
    @Inject
    lateinit var configSyncUseCase: ConfigSyncUseCase
    @Inject
    lateinit var crashController: CrashController
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var forceUpgradeNavigationComponent: ForceUpgradeNavigationComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Dashboard")

        setTheme(themeRes)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setSplashScreenTheme(themeRes)
        }
        val splashScreen = installSplashScreen()
        setTheme(themeRes)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AppTheme {
                HomeScreen(
                    windowSize = rememberWindowSizeClass(),
                    navigator = navigator,
                    closeApp = {
                        finish()
                    }
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
                forceUpgradeNavigationComponent.forceUpgrade()
                finish()
            }
        }

        // Deep links
        when (intent.extras?.getString("screen")) {
            "search" -> { navigator.navigate(Screen.Search) }
            "rss" -> { navigator.navigate(Screen.RSS) }
            else -> {}
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
    }

    override fun shouldKeepOnScreen(): Boolean = viewModel.appliedChanges
}
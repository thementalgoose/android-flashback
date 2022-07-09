package tmg.flashback.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.BuildConfig
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.forceupgrade.ForceUpgradeNavigationComponent
import tmg.flashback.statistics.workmanager.WorkerProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.utils.rememberWindowSizeClass
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.ui.dashboard.DashboardScreen
import tmg.flashback.ui.sync.SyncActivity

class HomeActivity: BaseActivity(), SplashScreen.KeepOnScreenCondition {

    private val viewModel: HomeViewModel by viewModel()

    private val workerProvider: WorkerProvider by inject()
    private val crashController: CrashController by inject()
    private val forceUpgradeNavigationComponent: ForceUpgradeNavigationComponent by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Dashboard")

        setTheme(themeRes)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setSplashScreenTheme(themeRes)
        }
        val splashScreen = installSplashScreen()
        setTheme(themeRes)

        setContent {
            AppTheme {
                DashboardScreen(windowSize = rememberWindowSizeClass())
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

        // Content sync
        try {
            workerProvider.contentSync()
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
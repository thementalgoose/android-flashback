package tmg.flashback.ui.dashboard

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.threeten.bp.Clock
import org.threeten.bp.LocalDateTime
import tmg.common.ui.forceupgrade.ForceUpgradeActivity
import tmg.core.ui.base.BaseActivity
import tmg.core.ui.model.DisplayType
import tmg.flashback.databinding.ActivityDashboardBinding
import tmg.flashback.ui.SyncActivity
import tmg.notifications.controllers.NotificationController

class HomeActivity: BaseActivity() {

    private lateinit var binding: ActivityDashboardBinding

    private val viewModel: HomeViewModel by viewModel()

    override val themeType: DisplayType = DisplayType.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(themeRes)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setSplashScreenTheme(themeRes)
        }
        val splashScreen = installSplashScreen()
        setTheme(themeRes)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        splashScreen.setKeepVisibleCondition {
            viewModel.appliedChanges
        }

        viewModel.initialise()

        when {
            viewModel.requiresSync -> {
                startActivity(Intent(this@HomeActivity, SyncActivity::class.java))
                finish()
            }
            viewModel.forceUpgrade -> {
                startActivity(Intent(this@HomeActivity, ForceUpgradeActivity::class.java))
                finish()
            }
        }
    }
}
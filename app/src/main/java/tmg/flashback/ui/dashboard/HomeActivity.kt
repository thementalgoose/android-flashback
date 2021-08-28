package tmg.flashback.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.common.repository.model.ForceUpgrade
import tmg.common.ui.forceupgrade.ForceUpgradeActivity
import tmg.core.ui.base.BaseActivity
import tmg.core.ui.model.DisplayType
import tmg.flashback.R
import tmg.flashback.databinding.ActivityDashboardBinding
import tmg.flashback.ui.SyncActivity
import tmg.utilities.extensions.loadFragment

class HomeActivity: BaseActivity() {

    private lateinit var binding: ActivityDashboardBinding

    private val viewModel: HomeViewModel by viewModel()

    override val themeType: DisplayType = DisplayType.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()

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
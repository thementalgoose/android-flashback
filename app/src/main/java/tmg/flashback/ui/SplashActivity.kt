package tmg.flashback.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.analytics.controllers.AnalyticsController
import tmg.flashback.databinding.ActivitySplashBinding
import tmg.flashback.ui.admin.forceupgrade.ForceUpgradeActivity
import tmg.flashback.statistics.ui.dashboard.DashboardActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.views.show

class SplashActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModel()

    private val analyticsManager: AnalyticsController by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observe(viewModel.outputs.showLoading) {
            binding.splash.show(it)
        }

        observe(viewModel.outputs.showResync) {
            binding.smiley.show(it)
            binding.tryAgain.show(it)
            binding.failedToSync.show(it)
        }

        observeEvent(viewModel.outputs.goToDashboard) {
            startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
            finish()
        }

        observeEvent(viewModel.outputs.goToForceUpgrade) {
            startActivity(Intent(this@SplashActivity, ForceUpgradeActivity::class.java))
            finish()
        }

        binding.tryAgain.setOnClickListener {
            viewModel.inputs.start()
        }

        viewModel.inputs.start()
    }

    override fun onResume() {
        super.onResume()

        analyticsManager.viewScreen(
                screenName = "Splash screen",
                clazz = this.javaClass,
                params = emptyMap()
        )
    }
}
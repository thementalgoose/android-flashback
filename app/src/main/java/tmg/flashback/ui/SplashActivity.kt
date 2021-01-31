package tmg.flashback.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.core.controllers.AnalyticsController
import tmg.flashback.ui.dashboard.DashboardActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.views.show

class SplashActivity: AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModel()

    private val analyticsManager: AnalyticsController by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        observe(viewModel.outputs.showLoading) {
            splash.show(it)
        }

        observe(viewModel.outputs.showResync) {
            smiley.show(it)
            tryAgain.show(it)
            failedToSync.show(it)
        }

        observeEvent(viewModel.outputs.goToNextScreen) {
            startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
            finish()
        }

        tryAgain.setOnClickListener {
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
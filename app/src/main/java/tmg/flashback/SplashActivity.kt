package tmg.flashback

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.home.HomeActivity
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.views.show

class SplashActivity: AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModel()

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
            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            finish()
        }

        tryAgain.setOnClickListener {
            viewModel.inputs.start()
        }

        viewModel.inputs.start()
    }
}
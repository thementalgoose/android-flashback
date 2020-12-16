package tmg.flashback

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import tmg.flashback.home.HomeActivity
import tmg.flashback.repo.config.RemoteConfigRepository

class SplashActivity: AppCompatActivity() {

    private val remoteConfigRepository: RemoteConfigRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch {

            remoteConfigRepository.activate()
            Log.i("Flashback", "${remoteConfigRepository.defaultYear}")
            runOnUiThread {
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                finish()
            }
        }
    }
}
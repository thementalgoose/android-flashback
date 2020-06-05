package tmg.flashback

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tmg.flashback.dashboard.DashboardActivity
import tmg.flashback.home.HomeActivity

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}
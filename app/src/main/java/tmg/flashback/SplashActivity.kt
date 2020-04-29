package tmg.flashback

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.inject
import tmg.flashback.base.BaseActivity
import tmg.flashback.dashboard.DashboardActivity
import tmg.flashback.home.static.HomeStaticActivity
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.enums.ViewTypePref

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}
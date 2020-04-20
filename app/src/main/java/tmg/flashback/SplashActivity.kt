package tmg.flashback

import android.content.Intent
import tmg.flashback.base.BaseActivity
import tmg.flashback.home.static.HomeStaticActivity
import tmg.flashback.repo.enums.ViewTypePref

class SplashActivity: BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_splash

    override fun initViews() {

        when (prefsDB.viewType) {
            ViewTypePref.STATIC -> {
                startActivity(Intent(this, HomeStaticActivity::class.java))
                finish()
            }
        }
    }
}
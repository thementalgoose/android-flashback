package tmg.f1stats

import android.content.Intent
import org.koin.android.ext.android.inject
import tmg.f1stats.base.BaseActivity
import tmg.f1stats.home.swiping.HomeSwipingActivity
import tmg.f1stats.home.static.HomeStaticActivity
import tmg.f1stats.repo.db.PrefsDB
import tmg.f1stats.repo.enums.ViewTypePref

class SplashActivity: BaseActivity() {

    private val prefsDB: PrefsDB by inject()

    override fun layoutId(): Int = R.layout.activity_splash

    override fun initViews() {

        when (prefsDB.viewType) {
            ViewTypePref.SWIPING -> {
                startActivity(Intent(this, HomeSwipingActivity::class.java))
                finish()
            }
            ViewTypePref.STATIC -> {
                startActivity(Intent(this, HomeStaticActivity::class.java))
                finish()
            }
        }
    }
}
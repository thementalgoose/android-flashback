package tmg.f1stats

import android.content.Intent
import tmg.f1stats.base.BaseActivity
import tmg.f1stats.home.HomeActivity
import tmg.utilities.extensions.startActivity

class SplashActivity: BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_splash

    override fun initViews() {

        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    override fun observeViewModel() {

    }

}
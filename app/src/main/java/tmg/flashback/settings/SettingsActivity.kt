package tmg.flashback.settings

import android.view.MenuItem
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.utilities.extensions.initToolbar

class SettingsActivity: BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_settings

    override fun initViews() {

        initToolbar(R.id.toolbar, true, indicator = R.drawable.ic_back)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
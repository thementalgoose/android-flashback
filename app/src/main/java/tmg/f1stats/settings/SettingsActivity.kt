package tmg.f1stats.settings

import android.view.MenuItem
import tmg.f1stats.R
import tmg.f1stats.base.BaseActivity
import tmg.utilities.extensions.initToolbar

class SettingsActivity: BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_settings

    override fun initViews() {

        initToolbar(R.id.toolbar, true)
    }

    override fun observeViewModel() { }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
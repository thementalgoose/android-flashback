package tmg.flashback.dashboard

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.toolbar.view.*
import org.koin.android.ext.android.inject
import tmg.flashback.R
import tmg.flashback.admin.lockout.LockoutActivity
import tmg.flashback.base.BaseActivity
import tmg.flashback.currentYear
import tmg.flashback.race.RaceActivity
import tmg.flashback.settings.SettingsActivity
import tmg.flashback.settings.release.ReleaseBottomSheetFragment
import tmg.utilities.extensions.initToolbar
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.startActivityClearStack

class DashboardActivity: BaseActivity() {

    private val viewModel: DashboardViewModel by inject()
    private val seasonAdapter: DashboardFragmentAdapter = DashboardFragmentAdapter(this)

    override fun layoutId(): Int = R.layout.activity_dashboard

    override fun initViews() {
        initToolbar(R.id.toolbar)
        toolbarLayout.header.text = getString(R.string.app_name)

        observeEvent(viewModel.outputs.showAppLockoutMessage) {
            startActivityClearStack(Intent(this, LockoutActivity::class.java))
        }

        observeEvent(viewModel.outputs.showReleaseNotes) {
            val instance = ReleaseBottomSheetFragment()
            instance.show(supportFragmentManager, "Release Notes")
        }

        setupViewPagers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_dashboard, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_settings -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.nav_all -> Toast.makeText(this, "All", Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViewPagers() {

        vpSeason.adapter = seasonAdapter

        TabLayoutMediator(tlMain, vpSeason) { tab, position ->
            tab.text = (currentYear - position).toString()
        }.attach()
    }
}
package tmg.f1stats.home

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.f1stats.R
import tmg.f1stats.base.BaseActivity
import tmg.f1stats.gallery.GalleryFragment
import tmg.f1stats.home.datepicker.DatePickerBottomSheetDialogFragment
import tmg.f1stats.season.SeasonFragment
import tmg.f1stats.settings.SettingsFragment
import tmg.f1stats.settings.sync.SettingsSyncActivity
import tmg.utilities.extensions.initToolbar
import tmg.utilities.extensions.loadFragment
import tmg.utilities.extensions.subscribeNoError

class HomeActivity: BaseActivity() {

    private val viewModel: HomeViewModel by viewModel()

    override fun layoutId(): Int = R.layout.activity_home

    override fun initViews() {
        loadFragment(SeasonFragment.newInstance(2019), R.id.flContainer, "SEASON")

        initToolbar(R.id.toolbar)
        supportActionBar?.title = "2019"

        bnvNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_drivers -> viewModel.clickTab(HomeTabOption.DRIVERS)
                R.id.nav_constructors -> viewModel.clickTab(HomeTabOption.CONSTRUCTORS)
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsSyncActivity::class.java))
                    viewModel.clickTab(HomeTabOption.SETTINGS)
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_calendar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_calendar -> {
                viewModel.inputs.clickCalendar()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun observeViewModel() {

        viewModel.outputs
            .showCalendarPicker()
            .subscribeNoError { year ->
                DatePickerBottomSheetDialogFragment
                    .instance(year)
                    .show(supportFragmentManager, "DatePicker")
            }
            .autoDispose()

        viewModel.outputs
            .showSeason()
            .subscribeNoError {
                // LOAD TAB
            }
            .autoDispose()

    }
}
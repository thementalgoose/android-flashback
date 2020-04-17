package tmg.f1stats.home.swiping

import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.f1stats.R
import tmg.f1stats.base.BaseActivity
import tmg.f1stats.home.datepicker.DatePickerBottomSheetDialogFragment
import tmg.f1stats.season.swiper.SeasonSwiperFragment
import tmg.f1stats.settings.SettingsFragment
import tmg.utilities.extensions.initToolbar
import tmg.utilities.extensions.loadFragment
import tmg.utilities.extensions.subscribeNoError

class HomeSwipingActivity: BaseActivity() {

    private val viewModel: HomeSwipingViewModel by viewModel()

    override fun layoutId(): Int = R.layout.activity_home

    override fun initViews() {

        initToolbar(R.id.toolbar)
        supportActionBar?.title = "2019"

        bnvNavigation.setOnNavigationItemSelectedListener {
//            when (it.itemId) {
//                R.id.nav_drivers -> viewModel.clickTab(HomeTabOption.DRIVERS)
//                R.id.nav_constructors -> viewModel.clickTab(HomeTabOption.CONSTRUCTORS)
//                R.id.nav_settings -> {
//                    viewModel.clickTab(HomeTabOption.SETTINGS)
//                }
//            }
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
//                viewModel.inputs.clickCalendar()
            }
        }
        return super.onOptionsItemSelected(item)
    }

//    override fun observeViewModel() {

//        viewModel.outputs
//            .showCalendarPicker()
//            .subscribeNoError { year ->
//                DatePickerBottomSheetDialogFragment
//                    .instance(year)
//                    .show(supportFragmentManager, "DatePicker")
//            }
//            .autoDispose()

//        viewModel.outputs
//            .showScreen()
//            .subscribeNoError {
//                when (it) {
//                    is Screen.Drivers -> {
//                        loadFragment(SeasonSwiperFragment.newInstance(it.season), R.id.flContainer, "DRIVERS")
//                    }
//                    is Screen.Constructor -> {
//                        loadFragment(SeasonSwiperFragment.newInstance(it.season), R.id.flContainer, "CONSTRUCTORS")
//                    }
//                    Screen.Settings -> {
//                        loadFragment(SettingsFragment(), R.id.flContainer, "SETTINGS")
//                    }
//                }
//            }
//            .autoDispose()

//    }
}
package tmg.f1stats.home

import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.f1stats.R
import tmg.f1stats.base.BaseActivity
import tmg.f1stats.gallery.GalleryFragment
import tmg.f1stats.season.SeasonFragment
import tmg.f1stats.settings.SettingsFragment
import tmg.utilities.extensions.loadFragment
import tmg.utilities.extensions.subscribeNoError

class HomeActivity: BaseActivity() {

    private val viewModel: HomeViewModel by viewModel()

    override fun layoutId(): Int = R.layout.activity_home

    override fun initViews() {
        loadFragment(SeasonFragment.newInstance(2019), R.id.flContainer, "SEASON")

        bnvNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_drivers -> viewModel.clickTab(HomeTabOption.DRIVERS)
                R.id.nav_constructors -> viewModel.clickTab(HomeTabOption.CONSTRUCTORS)
                R.id.nav_gallery -> viewModel.clickTab(HomeTabOption.GALLERY)
                R.id.nav_settings -> viewModel.clickTab(HomeTabOption.SETTINGS)
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun observeViewModel() {

    }
}
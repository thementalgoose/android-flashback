package tmg.f1stats.home

import tmg.f1stats.R
import tmg.f1stats.base.BaseActivity
import tmg.f1stats.home.season.SeasonFragment
import tmg.utilities.extensions.loadFragment

class HomeActivity: BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_home

    override fun initViews() {
        loadFragment(SeasonFragment.newInstance(2019), R.id.flContainer, "MAIN")
    }

    override fun observeViewModel() {

    }
}
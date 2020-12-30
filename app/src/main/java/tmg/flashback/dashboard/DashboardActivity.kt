package tmg.flashback.dashboard

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_dashboard.*
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.dashboard.list.ListFragment
import tmg.flashback.dashboard.search.SearchFragment
import tmg.flashback.dashboard.season.SeasonFragment
import tmg.utilities.extensions.loadFragment

class DashboardActivity: BaseActivity(), DashboardNavigationCallback {
    override fun layoutId() = R.layout.activity_dashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadFragment(SeasonFragment(), R.id.season, "season")
        loadFragment(ListFragment(), R.id.list, "list")
        loadFragment(SearchFragment(), R.id.search, "search")
    }

    override fun openSeasonList() {
        panels.openStartPanel()
    }

    override fun openSearch() {
        panels.openEndPanel()
    }
}
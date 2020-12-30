package tmg.flashback.dashboard

import android.os.Bundle
import com.discord.panels.OverlappingPanelsLayout
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.koin.android.ext.android.inject
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.dashboard.list.ListFragment
import tmg.flashback.dashboard.search.SearchFragment
import tmg.flashback.dashboard.season.SeasonFragment
import tmg.flashback.home.HomeMenuItem
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.rss.ui.RSSActivity
import tmg.utilities.extensions.loadFragment

class DashboardActivity: BaseActivity(), DashboardNavigationCallback {

    private val remoteConfigRepository: RemoteConfigRepository by inject()

    override fun layoutId() = R.layout.activity_dashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadFragment(SeasonFragment(), R.id.season, "season")
        loadFragment(ListFragment(), R.id.list, "list")
        loadFragment(SearchFragment(), R.id.search, "search")

        if (!remoteConfigRepository.search) {
            panels.setEndPanelLockState(lockState = OverlappingPanelsLayout.LockState.CLOSE)
        }
    }

    //region DashboardNavigationCallback

    override fun openSeasonList() {
        panels.openStartPanel()
    }

    override fun openSearch() {
        panels.openEndPanel()
    }

    override fun seasonSelected(season: Int) {
        val seasonFragment = supportFragmentManager.findFragmentByTag("season") as? SeasonFragment
        seasonFragment?.selectSeason(season)
    }

    override fun closeSeasonList() {
        panels.closePanels()
    }

    override fun closeSearch() {
        panels.closePanels()
    }

    //endregion
}
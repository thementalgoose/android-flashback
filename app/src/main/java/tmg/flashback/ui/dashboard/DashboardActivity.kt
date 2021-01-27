package tmg.flashback.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.transition.Transition
import android.util.Log
import android.widget.Toast
import androidx.annotation.StyleRes
import com.discord.panels.OverlappingPanelsLayout
import com.discord.panels.OverlappingPanelsLayout.Panel.CENTER
import com.discord.panels.OverlappingPanelsLayout.Panel.END
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.extensions.isLightMode
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.ui.dashboard.list.ListFragment
import tmg.flashback.ui.dashboard.search.SearchFragment
import tmg.flashback.ui.dashboard.season.SeasonFragment
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.ui.base.ThemeTypes
import tmg.flashback.ui.settings.release.ReleaseBottomSheetFragment
import tmg.utilities.extensions.loadFragment
import tmg.utilities.extensions.observeEvent

class DashboardActivity: BaseActivity(), DashboardNavigationCallback {

    override val initialiseSlidr: Boolean = false
    override val themeType: ThemeTypes = ThemeTypes.DEFAULT

    private var selectedSeason: Int? = null
    override val analyticsScreenName: String
        get() = "Dashboard"
    override val analyticsCustomAttributes: Map<String, String>
        get() = mapOf(
                "extra_season" to "$selectedSeason"
        )

    private val remoteConfigRepository: RemoteConfigRepository by inject()
    private val viewModel: DashboardViewModel by viewModel()

    private val seasonTag: String = "season"
    private val seasonFragment: SeasonFragment?
        get() = supportFragmentManager.findFragmentByTag(seasonTag) as? SeasonFragment
    private val listTag: String = "list"
    private val listFragment: ListFragment?
        get() = supportFragmentManager.findFragmentByTag(listTag) as? ListFragment
    private val searchTag: String = "search"
    private val searchFragment: SearchFragment?
        get() = supportFragmentManager.findFragmentByTag(searchTag) as? SearchFragment

    override fun layoutId() = R.layout.activity_dashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadFragment(SeasonFragment(), R.id.season, seasonTag)
        loadFragment(ListFragment(), R.id.list, listTag)
        loadFragment(SearchFragment(), R.id.search, searchTag)

        // Disable search functionality until toggled on
        if (!remoteConfigRepository.search) {
            panels.setEndPanelLockState(lockState = OverlappingPanelsLayout.LockState.CLOSE)
        }

        observeEvent(viewModel.outputs.openAppLockout) {
            startActivity(Intent(this, tmg.flashback.ui.admin.LockoutActivity::class.java))
            finishAffinity()
        }

        observeEvent(viewModel.outputs.openReleaseNotes) {
            ReleaseBottomSheetFragment()
                .show(supportFragmentManager, "releaseNotes")
        }

        observeEvent(viewModel.outputs.appConfigSynced) {
            listFragment?.refresh()
            seasonFragment?.refresh()
        }
    }

    override fun onBackPressed() {
        when {
            panels.getSelectedPanel() == END -> panels.closePanels()
            panels.getSelectedPanel() == CENTER -> panels.openStartPanel()
            else -> super.onBackPressed()
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
        seasonFragment?.let {
            it.selectSeason(season)
            selectedSeason = season
            recordScreenViewed()
        }
    }

    override fun closeSeasonList() {
        panels.closePanels()
    }

    override fun closeSearch() {
        panels.closePanels()
    }

    //endregion
}
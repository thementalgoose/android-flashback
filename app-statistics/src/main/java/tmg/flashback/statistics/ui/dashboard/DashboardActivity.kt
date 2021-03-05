package tmg.flashback.statistics.ui.dashboard

import android.os.Bundle
import com.discord.panels.OverlappingPanelsLayout
import com.discord.panels.OverlappingPanelsLayout.Panel.CENTER
import com.discord.panels.OverlappingPanelsLayout.Panel.END
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.core.controllers.FeatureController
import tmg.flashback.statistics.controllers.SeasonController
import tmg.flashback.core.enums.DisplayType
import tmg.flashback.core.managers.NavigationManager
import tmg.flashback.core.ui.BaseActivity
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ActivityDashboardBinding
import tmg.flashback.statistics.ui.dashboard.list.ListFragment
import tmg.flashback.statistics.ui.dashboard.search.SearchFragment
import tmg.flashback.statistics.ui.dashboard.season.SeasonFragment
import tmg.utilities.extensions.loadFragment
import tmg.utilities.extensions.observeEvent

class DashboardActivity: BaseActivity(), DashboardNavigationCallback {

    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: DashboardViewModel by viewModel()

    override val initialiseSlidr: Boolean = false
    override val themeType: DisplayType = DisplayType.DEFAULT

    private val featureController: FeatureController by inject()
    private val seasonController: SeasonController by inject()
    private val navigationManager: NavigationManager by inject()

    private var selectedSeason: Int? = seasonController.defaultSeason
    override val analyticsScreenName: String
        get() = "Dashboard"
    override val analyticsCustomAttributes: Map<String, String>
        get() = mapOf(
                "extra_season" to "$selectedSeason"
        )

    private val seasonTag: String = "season"
    private val seasonFragment: SeasonFragment?
        get() = supportFragmentManager.findFragmentByTag(seasonTag) as? SeasonFragment
    private val listTag: String = "list"
    private val listFragment: ListFragment?
        get() = supportFragmentManager.findFragmentByTag(listTag) as? ListFragment
    private val searchTag: String = "search"
    private val searchFragment: SearchFragment?
        get() = supportFragmentManager.findFragmentByTag(searchTag) as? SearchFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(SeasonFragment(), R.id.season, seasonTag)
        loadFragment(ListFragment(), R.id.list, listTag)
        loadFragment(SearchFragment(), R.id.search, searchTag)

        // Disable search functionality until toggled on
        if (!featureController.searchEnabled) {
            binding.panels.setEndPanelLockState(lockState = OverlappingPanelsLayout.LockState.CLOSE)
        }

        observeEvent(viewModel.outputs.openAppLockout) {
            startActivity(navigationManager.getMaintenanceIntent(this))
            finishAffinity()
        }

        observeEvent(viewModel.outputs.appConfigSynced) {
            listFragment?.refresh()
            seasonFragment?.refresh()
        }

        navigationManager.openContextualReleaseNotes(this)
    }

    override fun onBackPressed() {
        when {
            binding.panels.getSelectedPanel() == END -> binding.panels.closePanels()
            binding.panels.getSelectedPanel() == CENTER -> binding.panels.openStartPanel()
            else -> super.onBackPressed()
        }
    }

    //region DashboardNavigationCallback

    override fun openSeasonList() {
        binding.panels.openStartPanel()
    }

    override fun openSearch() {
        binding.panels.openEndPanel()
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
        binding.panels.closePanels()
    }

    override fun closeSearch() {
        binding.panels.closePanels()
    }

    //endregion
}
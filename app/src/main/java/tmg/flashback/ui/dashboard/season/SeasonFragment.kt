package tmg.flashback.ui.dashboard.season

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dashboard_season.*
import kotlinx.android.synthetic.main.fragment_dashboard_season.dataList
import kotlinx.android.synthetic.main.fragment_dashboard_season.season
import kotlinx.android.synthetic.main.fragment_dashboard_season.swipeContainer
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.constants.App.currentYear
import tmg.flashback.ui.base.BaseFragment
import tmg.flashback.ui.dashboard.DashboardNavigationCallback
import tmg.flashback.ui.overviews.constructor.ConstructorActivity
import tmg.flashback.ui.overviews.driver.DriverActivity
import tmg.flashback.ui.race.RaceActivity
import tmg.flashback.rss.ui.RSSActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.views.visible

class SeasonFragment: BaseFragment() {

    private lateinit var adapter: SeasonAdapter
    private var dashboardNavigation: DashboardNavigationCallback? = null

    private val viewModel: SeasonViewModel by viewModel()

    override fun layoutId() = R.layout.fragment_dashboard_season

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DashboardNavigationCallback) {
            dashboardNavigation = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeContainer.isEnabled = false
        adapter = SeasonAdapter(
            trackClicked = viewModel.inputs::clickTrack,
            driverClicked = viewModel.inputs::clickDriver,
            constructorClicked = viewModel.inputs::clickConstructor
        )
        dataList.layoutManager = LinearLayoutManager(context)
        dataList.adapter = adapter

        if (remoteConfigRepository.search) {
            searchButton.visible()
        }
        navigation.setOnNavigationItemSelectedListener {
            return@setOnNavigationItemSelectedListener when (it.itemId) {
                R.id.nav_rss -> {
                    context?.let { context -> startActivity(RSSActivity.intent(context)) }
                    false
                }
                R.id.nav_calendar -> {
                    viewModel.inputs.clickItem(SeasonNavItem.CALENDAR)
                    true
                }
                R.id.nav_drivers -> {
                    viewModel.inputs.clickItem(SeasonNavItem.DRIVERS)
                    true
                }
                R.id.nav_constructor -> {
                    viewModel.inputs.clickItem(SeasonNavItem.CONSTRUCTORS)
                    true
                }
                else -> false
            }
        }

        menuButton.setOnClickListener {
            viewModel.inputs.clickMenu()
        }
        searchButton.setOnClickListener {
            viewModel.inputs.clickSearch()
        }


        observeEvent(viewModel.outputs.openMenu) {
            dashboardNavigation?.openSeasonList()
        }

        observe(viewModel.outputs.label) {
            season.text = getString(R.string.home_season_arrow, it.msg ?: currentYear.toString())
        }

        observeEvent(viewModel.outputs.openSearch) {
            dashboardNavigation?.openSearch()
        }

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observe(viewModel.outputs.showLoading) {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        }

        observeEvent(viewModel.outputs.openRace) { track ->
            context?.let {
                val intent = RaceActivity.intent(
                    context = it,
                    season = track.season,
                    round = track.round,
                    circuitId = track.circuitId,
                    country = track.raceCountry,
                    raceName = track.raceName,
                    trackName = track.circuitName,
                    countryISO = track.raceCountryISO,
                    date = track.date,
                    defaultToRace = track.hasResults || !track.hasQualifying
                )
                startActivity(intent)
            }
        }
        observeEvent(viewModel.outputs.openDriver) { driver ->
            context?.let {
                val intent = DriverActivity.intent(
                    context = it,
                    driverId = driver.driverId,
                    driverName = driver.driver.name
                )
                startActivity(intent)
            }
        }
        observeEvent(viewModel.outputs.openConstructor) { constructor ->
            context?.let {
                val intent = ConstructorActivity.intent(
                    context = it,
                    constructorId = constructor.constructorId,
                    constructorName = constructor.constructor.name
                )
                startActivity(intent)
            }
        }

        showLoading()

        navigation.selectedItemId = R.id.nav_calendar
    }

    //region Accessible

    /**
     * Publicly accessible method for changing which season this fragment displays
     *  Called from DashboardActivity as a result of an external season selection
     */
    fun selectSeason(season: Int) {
        viewModel.inputs.selectSeason(season)
    }

    /**
     * Publically accessible method for refreshing the season
     */
    fun refresh() {
        viewModel.inputs.refresh()
    }

    //endregion

    private fun showLoading() {
        swipeContainer.isRefreshing = true
        searchButton.isEnabled = false
        menuButton.isEnabled = false
        navigation.isEnabled = false
        dataList.alpha = 0.7f
    }

    private fun hideLoading() {
        swipeContainer.isRefreshing = false
        searchButton.isEnabled = true
        menuButton.isEnabled = true
        navigation.isEnabled = true
        dataList.alpha = 1.0f
    }
}
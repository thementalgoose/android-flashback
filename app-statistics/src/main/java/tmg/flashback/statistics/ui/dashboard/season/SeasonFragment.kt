package tmg.flashback.statistics.ui.dashboard.season

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.core.ui.BaseFragment
import tmg.flashback.statistics.R
import tmg.flashback.statistics.constants.Formula1.currentSeasonYear
import tmg.flashback.statistics.databinding.FragmentDashboardSeasonBinding
import tmg.flashback.statistics.ui.dashboard.DashboardFragment
import tmg.flashback.statistics.ui.dashboard.DashboardNavigationCallback
import tmg.flashback.statistics.ui.overview.constructor.ConstructorActivity
import tmg.flashback.statistics.ui.overview.driver.DriverActivity
import tmg.flashback.statistics.ui.race.RaceActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class SeasonFragment: BaseFragment<FragmentDashboardSeasonBinding>() {

    private val viewModel: SeasonViewModel by viewModel()

    private lateinit var adapter: SeasonAdapter
    private val dashboardNavigation: DashboardNavigationCallback?
        get() = parentFragment as? DashboardFragment

    override fun inflateView(inflater: LayoutInflater) =
        FragmentDashboardSeasonBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SeasonAdapter(
            trackClicked = viewModel.inputs::clickTrack,
            driverClicked = viewModel.inputs::clickDriver,
            constructorClicked = viewModel.inputs::clickConstructor
        )
        binding.dataList.layoutManager = LinearLayoutManager(context)
        binding.dataList.adapter = adapter

        binding.menu.setOnClickListener {
            viewModel.inputs.clickMenu()
        }

        observeEvent(viewModel.outputs.openMenu) {
            dashboardNavigation?.openSeasonList()
        }

        observe(viewModel.outputs.label) {
            binding.seasonCollapsed.text = getString(R.string.home_season_arrow, it.msg ?: currentSeasonYear.toString())
            binding.seasonExpanded.text = getString(R.string.home_season_arrow, it.msg ?: currentSeasonYear.toString())
        }

        observe(viewModel.outputs.list) {
            adapter.list = it
            binding.dataList.smoothScrollToPosition(0)
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
     * Publicaly accessible method for changing the display type for the list to be calendar
     *  Called from DashboardActivity as a result of moving nav bar to activity for
     */
    fun selectSchedule() {
        viewModel.inputs.clickItem(SeasonNavItem.SCHEDULE)
    }

    /**
     * Publicaly accessible method for changing the display type for the list to be drivers
     *  Called from DashboardActivity as a result of moving nav bar to activity for
     */
    fun selectDrivers() {
        viewModel.inputs.clickItem(SeasonNavItem.DRIVERS)
    }

    /**
     * Publicaly accessible method for changing the display type for the list to be constructors
     *  Called from DashboardActivity as a result of moving nav bar to activity for
     */
    fun selectConstructors() {
        viewModel.inputs.clickItem(SeasonNavItem.CONSTRUCTORS)
    }

    /**
     * Publically accessible method for refreshing the season
     */
    fun refresh() {
        viewModel.inputs.refresh()
    }

    //endregion

    private fun showLoading() {
//        binding.swipeContainer.isRefreshing = true
//        binding.menuButton.isEnabled = false
        binding.dataList.alpha = 0.7f
    }

    private fun hideLoading() {
//        binding.swipeContainer.isRefreshing = false
//        binding.menuButton.isEnabled = true
        binding.dataList.alpha = 1.0f
    }
}
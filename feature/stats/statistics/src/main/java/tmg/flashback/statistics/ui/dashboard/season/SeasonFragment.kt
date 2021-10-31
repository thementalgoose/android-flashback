package tmg.flashback.statistics.ui.dashboard.season

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.IMPORTANT_FOR_ACCESSIBILITY_NO
import android.view.View.IMPORTANT_FOR_ACCESSIBILITY_YES
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDate
import tmg.core.ui.base.BaseFragment
import tmg.flashback.statistics.R
import tmg.flashback.formula1.constants.Formula1.currentSeasonYear
import tmg.flashback.statistics.controllers.SeasonController
import tmg.flashback.statistics.databinding.FragmentDashboardSeasonBinding
import tmg.flashback.statistics.ui.overview.constructor.ConstructorActivity
import tmg.flashback.statistics.ui.overview.driver.DriverActivity
import tmg.flashback.statistics.ui.race.RaceActivity
import tmg.flashback.statistics.ui.race.RaceData
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.views.show

class SeasonFragment: BaseFragment<FragmentDashboardSeasonBinding>() {

    private val viewModel: SeasonViewModel by viewModel()

    private val seasonController: SeasonController by inject()
    private val analyticsData: MutableMap<String, String> = mutableMapOf(
        "extra_season_id" to seasonController.defaultSeason.toString(),
        "extra_view_type" to SeasonNavItem.SCHEDULE.name
    )

    private lateinit var adapter: SeasonAdapter
    private val seasonFragmentCallback: SeasonFragmentCallback?
        get() = parentFragment as? SeasonFragmentCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Dashboard", analyticsData)
    }

    override fun inflateView(inflater: LayoutInflater) =
        FragmentDashboardSeasonBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SeasonAdapter(
            trackClicked = viewModel.inputs::clickTrack,
            driverClicked = viewModel.inputs::clickDriver,
            constructorClicked = viewModel.inputs::clickConstructor,
            calendarWeekRaceClicked = { week ->
                week.race?.let {
                    viewModel.inputs.clickTrack(SeasonItem.Track(
                        season = it.season,
                        raceName = it.raceName,
                        circuitName = it.circuitName,
                        circuitId = it.circuitId,
                        raceCountry = it.country,
                        raceCountryISO = it.countryISO,
                        date = it.date,
                        round = it.round,
                        hasQualifying = it.hasQualifying,
                        hasResults = it.hasResults,
                    ))
                }
            }
        )
        binding.dataList.layoutManager = LinearLayoutManager(context)
        binding.dataList.adapter = adapter

        binding.dataList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    seasonFragmentCallback?.scrollDown()
                } else if (dy < 0) {
                    seasonFragmentCallback?.scrollUp()
                }
            }
        })

        binding.menu.setOnClickListener {
            viewModel.inputs.clickMenu()
        }

        binding.upNextContainer.setOnClickListener {
            viewModel.inputs.clickNow()
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.inputs.refresh()
        }

        observeEvent(viewModel.outputs.openMenu) {
            seasonFragmentCallback?.openMenu()
        }

        observeEvent(viewModel.outputs.openNow) {
            seasonFragmentCallback?.openNow()
        }

        observe(viewModel.outputs.showUpNext) {
            binding.upNextContainer.show(it)
        }

        observe(viewModel.outputs.label) {
            binding.titleCollapsed.text = getString(R.string.home_season_arrow, it.msg ?: currentSeasonYear.toString())
            binding.titleExpanded.text = getString(R.string.home_season_arrow, it.msg ?: currentSeasonYear.toString())
        }

        observe(viewModel.outputs.list) { list ->
            adapter.list = list
            if (list.any { it is SeasonItem.CalendarMonth && it.year == currentSeasonYear }) {
                val currentMonth = LocalDate.now().month
                val positionToScrollTo = list
                    .indexOfFirst { it is SeasonItem.CalendarMonth && it.month == currentMonth }

                (binding.dataList.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(positionToScrollTo.coerceAtLeast(0), 0)
            }
            else {
                binding.dataList.smoothScrollToPosition(0)
            }
        }

        observe(viewModel.outputs.showLoading) {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        }

        observeEvent(viewModel.outputs.showRefreshError) {
//            Toast.makeText(context, getString(R.string.))
        }

        observeEvent(viewModel.outputs.openRace) { track ->
            context?.let {
                val intent = RaceActivity.intent(it, RaceData(
                    season = track.season,
                    round = track.round,
                    circuitId = track.circuitId,
                    country = track.raceCountry,
                    raceName = track.raceName,
                    trackName = track.circuitName,
                    countryISO = track.raceCountryISO,
                    date = track.date,
                    defaultToRace = track.hasResults || !track.hasQualifying
                ))
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
        analyticsData["extra_season_id"] = season.toString()
        logScreenViewed("Dashboard", analyticsData)
        viewModel.inputs.selectSeason(season)
    }

    /**
     * Publicaly accessible method for changing the display type for the list to be calendar
     *  Called from DashboardActivity as a result of moving nav bar to activity for
     */
    fun selectCalendar() {
        analyticsData["extra_view_type"] = SeasonNavItem.CALENDAR.name
        logScreenViewed("Dashboard", analyticsData)
        viewModel.inputs.clickItem(SeasonNavItem.CALENDAR)
    }

    /**
     * Publicaly accessible method for changing the display type for the list to be calendar
     *  Called from DashboardActivity as a result of moving nav bar to activity for
     */
    fun selectSchedule() {
        analyticsData["extra_view_type"] = SeasonNavItem.SCHEDULE.name
        logScreenViewed("Dashboard", analyticsData)
        viewModel.inputs.clickItem(SeasonNavItem.SCHEDULE)
    }

    /**
     * Publicaly accessible method for changing the display type for the list to be drivers
     *  Called from DashboardActivity as a result of moving nav bar to activity for
     */
    fun selectDrivers() {
        analyticsData["extra_view_type"] = SeasonNavItem.DRIVERS.name
        logScreenViewed("Dashboard", analyticsData)
        viewModel.inputs.clickItem(SeasonNavItem.DRIVERS)
    }

    /**
     * Publicaly accessible method for changing the display type for the list to be constructors
     *  Called from DashboardActivity as a result of moving nav bar to activity for
     */
    fun selectConstructors() {
        analyticsData["extra_view_type"] = SeasonNavItem.CONSTRUCTORS.name
        logScreenViewed("Dashboard", analyticsData)
        viewModel.inputs.clickItem(SeasonNavItem.CONSTRUCTORS)
    }

    /**
     * Publically accessible method for telling the season to show the up next prompt
     */
    fun showUpNext(value: Boolean) {
        viewModel.inputs.showUpNext(value)
    }

    /**
     * Publicaly accessible method for refreshing the season
     */
    fun refresh() {
        viewModel.inputs.appConfigSynced()
    }

    //endregion

    private fun showLoading() {
        // Sub-optimal workaround for visibility issue in motion layout
        binding.progress.importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_YES
        binding.progress.alpha = 1.0f

        binding.dataList.locked = true
        binding.dataList.alpha = dataListAlpha

        binding.swipeRefresh.isRefreshing = true
    }

    private fun hideLoading() {
        // Sub-optimal workaround for visibility issue in motion layout
        binding.progress.importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
        binding.progress.alpha = 0.0f

        binding.dataList.locked = false
        binding.dataList.alpha = 1.0f

        binding.swipeRefresh.isRefreshing = false
    }

    companion object {
        private const val dataListAlpha = 0.5f
    }
}
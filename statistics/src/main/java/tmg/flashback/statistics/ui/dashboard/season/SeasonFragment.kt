package tmg.flashback.statistics.ui.dashboard.season

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import tmg.flashback.formula1.constants.Formula1.currentSeasonYear
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.statistics.R
import tmg.flashback.statistics.controllers.HomeController
import tmg.flashback.statistics.databinding.FragmentDashboardSeasonBinding
import tmg.flashback.statistics.ui.dashboard.events.EventListBottomSheetFragment
import tmg.flashback.statistics.ui.dashboard.racepreview.RacePreviewBottomSheetFragment
import tmg.flashback.statistics.ui.overview.constructor.ConstructorActivity
import tmg.flashback.statistics.ui.overview.driver.DriverActivity
import tmg.flashback.statistics.ui.race.RaceActivity
import tmg.flashback.statistics.ui.race.RaceData
import tmg.flashback.ui.base.BaseFragment
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.views.invisible
import tmg.utilities.extensions.views.visible
import tmg.utilities.lifecycle.viewInflateBinding

class SeasonFragment: BaseFragment() {

    private val viewModel: SeasonViewModel by viewModel()
    private val binding by viewInflateBinding(FragmentDashboardSeasonBinding::inflate)

    private val homeController: HomeController by inject()
    private val analyticsData: MutableMap<String, String> = mutableMapOf(
        "extra_season_id" to homeController.defaultSeason.toString(),
        "extra_view_type" to SeasonNavItem.SCHEDULE.name
    )

    private lateinit var adapter: SeasonAdapter
    private val seasonFragmentCallback: SeasonFragmentCallback?
        get() = parentFragment as? SeasonFragmentCallback

    private var cachedOverviewRace: OverviewRace? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Dashboard", analyticsData)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SeasonAdapter(
            trackClicked = viewModel.inputs::clickTrack,
            driverClicked = viewModel.inputs::clickDriver,
            constructorClicked = viewModel.inputs::clickConstructor,
            calendarWeekRaceClicked = { week ->
                week.race?.let {
                    if (it.date > LocalDate.now()) {
                        cachedOverviewRace = it
                        RacePreviewBottomSheetFragment
                            .instance(it.season, it.round)
                            .show(parentFragmentManager, "RACE_PREVIEW_${it.season}_${it.round}")
                    } else {
                        clickTrack(it)
                    }
                }
            },
            eventTypeClicked = { season, type ->
                EventListBottomSheetFragment
                    .instance(season, type)
                    .show(parentFragmentManager, "EVENT_TYPE_${season}_${type.ordinal}")
            }
        )
        binding.dataList.layoutManager = LinearLayoutManager(context)
        binding.dataList.adapter = adapter
        binding.progress.invisible()

        setFragmentResultListener(RacePreviewBottomSheetFragment.fragmentResultKey) { _, bundle ->
            val shouldLoadRace = RacePreviewBottomSheetFragment.shouldLoadRace(bundle)
            if (shouldLoadRace && cachedOverviewRace != null) {
                clickTrack(cachedOverviewRace!!)
            }
        }

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

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.inputs.refresh()
            binding.progress.visible()
            seasonFragmentCallback?.refresh()
        }

        observeEvent(viewModel.outputs.openMenu) {
            seasonFragmentCallback?.openMenu()
        }

        observe(viewModel.outputs.label) {
            binding.titleCollapsed.text = getString(R.string.home_season_arrow, it)
            binding.titleExpanded.text = getString(R.string.home_season_arrow, it)
        }

        observe(viewModel.outputs.list) { list ->
            adapter.list = list
            seasonFragmentCallback?.refresh()
            val positionToScrollTo = if (homeController.dashboardAutoscroll) {
                when {
                    list.any { it is SeasonItem.Track && it.defaultExpanded && it.round > 1 } -> {
                        val positionToScrollTo = list
                            .indexOfFirst { it is SeasonItem.Track && it.defaultExpanded }
                        positionToScrollTo
                    }
                    list.any { it is SeasonItem.CalendarMonth && it.year == currentSeasonYear && it.month != Month.JANUARY } -> {
                        val currentMonth = LocalDate.now().month
                        val positionToScrollTo = list.indexOfFirst { it is SeasonItem.CalendarMonth && it.month == currentMonth }
                        positionToScrollTo
                    }
                    else -> 0
                }
            }
            else {
                0
            }

            if (positionToScrollTo == 0) {
                binding.dataList.smoothScrollToPosition(0)
            } else {
                binding.container.transitionToEnd()
                (binding.dataList.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(positionToScrollTo.coerceAtLeast(0), 0)
            }
        }

        observe(viewModel.outputs.showLoading) {
            if (it) {
                binding.progress.visible()
            } else {
                binding.swipeRefresh.isRefreshing = false
                binding.progress.invisible()
            }
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
                )
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
    }

    private fun clickTrack(it: OverviewRace) {
        viewModel.inputs.clickTrack(
            SeasonItem.Track(
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
                defaultExpanded = true,
                schedule = it.schedule
            )
        )
        cachedOverviewRace = null
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

    //endregion

    companion object {
        private const val dataListAlpha = 0.5f
    }
}
package tmg.flashback.statistics.ui.dashboard.season

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.temporal.TemporalAdjusters
import tmg.core.analytics.manager.AnalyticsManager
import tmg.flashback.formula1.constants.Formula1.constructorChampionshipStarts
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tmg.core.device.managers.NetworkConnectivityManager
import tmg.core.ui.controllers.ThemeController
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.statistics.R
import tmg.flashback.formula1.constants.Formula1.currentSeasonYear
import tmg.flashback.formula1.model.*
import tmg.flashback.statistics.controllers.SeasonController
import tmg.flashback.statistics.extensions.analyticsLabel
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.statistics.repo.SeasonRepository
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable
import tmg.utilities.extensions.combinePair
import tmg.utilities.extensions.then
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event
import tmg.utilities.models.StringHolder

//region Inputs

interface SeasonViewModelInputs {
    fun clickMenu()
    fun clickNow()
    fun clickItem(item: SeasonNavItem)

    fun refresh()

    fun showUpNext(value: Boolean = true)
    fun appConfigSynced()
    fun selectSeason(season: Int)

    fun clickTrack(track: SeasonItem.Track)
    fun clickDriver(driver: SeasonItem.Driver)
    fun clickConstructor(constructor: SeasonItem.Constructor)
}

//endregion

//region Outputs

interface SeasonViewModelOutputs {
    val openMenu: LiveData<Event>
    val openNow: LiveData<Event>
    val showUpNext: LiveData<Boolean>

    val openRace: LiveData<DataEvent<SeasonItem.Track>>
    val openDriver: LiveData<DataEvent<SeasonItem.Driver>>
    val openConstructor: LiveData<DataEvent<SeasonItem.Constructor>>

    val showRefreshError: LiveData<Event>

    val showLoading: LiveData<Boolean>
    val label: LiveData<StringHolder>
    val list: LiveData<List<SeasonItem>>
}

//endregion

class SeasonViewModel(
    private val themeController: ThemeController,
    private val overviewRepository: OverviewRepository,
    private val seasonRepository: SeasonRepository,
    private val seasonController: SeasonController,
    private val networkConnectivityManager: NetworkConnectivityManager,
    private val analyticsManager: AnalyticsManager
): ViewModel(), SeasonViewModelInputs, SeasonViewModelOutputs {

    private val currentTab: MutableStateFlow<SeasonNavItem> = MutableStateFlow(SeasonNavItem.SCHEDULE)
    private val season: MutableStateFlow<Int> = MutableStateFlow(seasonController.defaultSeason)

    override val openRace: MutableLiveData<DataEvent<SeasonItem.Track>> = MutableLiveData()
    override val openDriver: MutableLiveData<DataEvent<SeasonItem.Driver>> = MutableLiveData()
    override val openConstructor: MutableLiveData<DataEvent<SeasonItem.Constructor>> = MutableLiveData()

    override val showRefreshError: MutableLiveData<Event> = MutableLiveData()

    override val showLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    override val openMenu: MutableLiveData<Event> = MutableLiveData()
    override val openNow: MutableLiveData<Event> = MutableLiveData()
    override val showUpNext: MutableLiveData<Boolean> = MutableLiveData(false)

    /**
     * Label to be shown at the top of the screen to indicate what year it is
     */
    override val label: LiveData<StringHolder> = season
        .map { season ->
            StringHolder(msg = season.toString())
        }
        .asLiveData(viewModelScope.coroutineContext)

    /**
     * List to handle season data
     * - CALENDAR
     * - DRIVERS
     * - CONSTRUCTORS
     */
    private val seasonList: Flow<List<SeasonItem>> = season
        .combinePair(currentTab)
        .flatMapLatest { (season, menuItem) ->
            analyticsManager.logEvent(menuItem.analyticsLabel, mapOf(
                "season" to season.toString()
            ))

            val appBannerMessage = seasonController.banner

            return@flatMapLatest when (menuItem) {
                SeasonNavItem.CALENDAR -> {
                    overviewRepository.getOverview(season)
                        .map {
                            val list: MutableList<SeasonItem> = getBannerList()
                            when {
                                it.overviewRaces.isEmpty() && !networkConnectivityManager.isConnected ->
                                    list.addError(SyncDataItem.NoNetwork)
                                it.overviewRaces.isEmpty() && season == currentSeasonYear ->
                                    list.addError(SyncDataItem.Unavailable(DataUnavailable.EARLY_IN_SEASON))
                                it.overviewRaces.isEmpty() ->
                                    list.addError(SyncDataItem.Unavailable(DataUnavailable.MISSING_RACE))
                                else ->
                                    list.addAll(it.overviewRaces.toCalendar(season))
                            }
                            return@map list
                        }
                }
                SeasonNavItem.SCHEDULE -> {
                    overviewRepository.getOverview(season)
                        .map {
                            val list: MutableList<SeasonItem> = getBannerList()
                            when {
                                it.overviewRaces.isEmpty() && !networkConnectivityManager.isConnected ->
                                    list.addError(SyncDataItem.NoNetwork)
                                it.overviewRaces.isEmpty() && season == currentSeasonYear ->
                                    list.addError(SyncDataItem.Unavailable(DataUnavailable.EARLY_IN_SEASON))
                                it.overviewRaces.isEmpty() ->
                                    list.addError(SyncDataItem.Unavailable(DataUnavailable.MISSING_RACE))
                                else ->
                                    list.addAll(it.overviewRaces.toScheduleList())
                            }
                            return@map list
                        }

                }
                SeasonNavItem.DRIVERS -> {
                    seasonRepository.getDriverStandings(season)
                        .map { standings ->
                            val list: MutableList<SeasonItem> = getBannerList()
                            val results = standings?.standings ?: emptyList()
                            when {
                                results.isEmpty() && !networkConnectivityManager.isConnected ->
                                    list.addError(SyncDataItem.NoNetwork)
                                results.isEmpty() ->
                                    list.addError(SyncDataItem.Unavailable(DataUnavailable.IN_FUTURE_SEASON))
                                else -> {
                                    if (results.any { it.inProgress }) {
                                        list.addError(SyncDataItem.MessageRes(R.string.results_accurate_for, listOf(results.first().races)))
                                    }
                                    list.addAll(standings?.toDriverList() ?: emptyList())
                                }
                            }
                            return@map list
                        }
                }
                SeasonNavItem.CONSTRUCTORS -> {
                    seasonRepository.getConstructorStandings(season)
                        .map { standings ->
                            val list: MutableList<SeasonItem> = getBannerList()
                            val results = standings?.standings ?: emptyList()
                            when {
                                season < constructorChampionshipStarts ->
                                    list.addError(SyncDataItem.ConstructorsChampionshipNotAwarded)
                                results.isEmpty() && !networkConnectivityManager.isConnected ->
                                    list.addError(SyncDataItem.NoNetwork)
                                results.isEmpty() ->
                                    list.addError(SyncDataItem.Unavailable(DataUnavailable.IN_FUTURE_SEASON))
                                else -> {
                                    if (results.any { it.inProgress }) {
                                        list.addError(SyncDataItem.MessageRes(R.string.results_accurate_for, listOf(results.first().races)))
                                    }
                                    list.addAll(standings?.toConstructorList() ?: emptyList())
                                }
                            }
                            return@map list
                        }
                }
            }
        }
        .onStart { emitAll(flow { emptyList<SeasonItem>() }) }

    /**
     * Overview list that gets returned to the Activity
     */
    override val list: LiveData<List<SeasonItem>> = seasonList
        .then {
            showLoading.value = false
        }
        .asLiveData(viewModelScope.coroutineContext)

    var inputs: SeasonViewModelInputs = this
    var outputs: SeasonViewModelOutputs = this

    init {

    }

    //region Inputs

    override fun clickMenu() {
        openMenu.value = Event()
    }

    override fun showUpNext(value: Boolean) {
        showUpNext.value = value
    }

    override fun clickNow() {
        openNow.value = Event()
    }

    override fun clickItem(item: SeasonNavItem) {
        if (item != currentTab.value) {
            showLoading.value = true
            currentTab.value = item
        }
    }

    override fun appConfigSynced() {
        this.season.value = this.season.value
    }

    override fun refresh() {
        showLoading.value = true
        viewModelScope.launch(context = Dispatchers.IO) {
            val result = overviewRepository.fetchOverview(season.value)
            val anotherResult = seasonRepository.fetchRaces(season.value)
            showLoading.postValue(false)
            if (!result) {
                showRefreshError.postValue(Event())
            }
        }
    }

    override fun selectSeason(season: Int) {
        showLoading.value = true
        this.season.value = season
    }

    override fun clickTrack(track: SeasonItem.Track) {
        openRace.value = DataEvent(track)
    }

    override fun clickDriver(driver: SeasonItem.Driver) {
        openDriver.value = DataEvent(driver)
    }

    override fun clickConstructor(constructor: SeasonItem.Constructor) {
        openConstructor.value = DataEvent(constructor)
    }

    //endregion

    private fun getBannerList(): MutableList<SeasonItem> {
        val list = mutableListOf<SeasonItem>()
        seasonController.banner?.let {
            list.addError(SyncDataItem.Message(it.message, it.url))
        }
        return list
    }

    /**
     * Extract the calendar of events out into a formatted display list
     */
    private fun List<OverviewRace>.toCalendar(season: Int): List<SeasonItem> {
        val list = mutableListOf<SeasonItem>()
        list.add(SeasonItem.CalendarHeader)
        Month.values().forEach { month ->
            var start = LocalDate.of(season, month.value, 1)
            var end = when {
                start.dayOfWeek == DayOfWeek.SUNDAY -> {
                    LocalDate.of(season, month.value, 1)
                }
                else -> {
                    start.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
                }
            }

            list.add(SeasonItem.CalendarMonth(month, season))
            list.add(SeasonItem.CalendarWeek(month, start, this.firstOrNull { it.date >= start && it.date <= end }))
            while (start.month == month) {
                start = start.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                end = end.plusDays(7L)

                if (start.month == month) {
                    list.add(
                        SeasonItem.CalendarWeek(month, start, this.firstOrNull { it.date >= start && it.date <= end })
                    )
                }
            }
        }

        return list
    }

    /**
     * Extract the schedule of tracks out into a list of home items to display on the home screen
     */
    private fun List<OverviewRace>.toScheduleList(): List<SeasonItem> {
        return this
            .sortedBy { it.round }
            .map {
                SeasonItem.Track(
                    season = it.season,
                    round = it.round,
                    raceName = it.raceName,
                    circuitId = it.circuitId,
                    circuitName = it.circuitName,
                    raceCountry = it.country,
                    raceCountryISO = it.countryISO,
                    date = it.date,
                    hasQualifying = it.hasQualifying,
                    hasResults = it.hasResults
                )
            }
    }

    /**
     * Convert the driver standings construct into a list of home items to display on the home page
     * Compatibility function
     */
    private fun SeasonDriverStandings.toDriverList(): List<SeasonItem> {
        return this
            .standings
            .mapIndexed { index: Int, standing: SeasonDriverStandingSeason ->
                SeasonItem.Driver(
                    season = standing.season,
                    driver = standing.driver,
                    constructors = standing.constructors.map { it.constructor },
                    points = standing.points,
                    position = index + 1,
                    maxPointsInSeason = this.standings.maxByOrNull { it.points }?.points ?: 1000.0,
                    animationSpeed = themeController.animationSpeed
                )
            }
    }

    /**
     * Convert the constructor standings construct into a list of home items to display on the home page
     */
    private fun SeasonConstructorStandings.toConstructorList(): List<SeasonItem> {
        return this
            .standings
            .mapIndexed { index: Int, item: SeasonConstructorStandingSeason ->
                SeasonItem.Constructor(
                    season = item.season,
                    position = index + 1,
                    constructor = item.constructor,
                    driver = item.drivers
                        .map { Pair(it.driver, it.points) }
                        .sortedByDescending { it.second },
                    points = item.points,
                    maxPointsInSeason = this.standings.maxByOrNull { it.points }?.points ?: 1000.0,
                    barAnimation = themeController.animationSpeed
                )
            }
            .sortedByDescending { it.points }
    }
}

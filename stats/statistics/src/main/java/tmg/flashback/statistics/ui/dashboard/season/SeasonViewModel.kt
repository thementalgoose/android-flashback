package tmg.flashback.statistics.ui.dashboard.season

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.temporal.TemporalAdjusters
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.ui.controllers.ThemeController
import tmg.flashback.formula1.constants.Formula1.currentSeasonYear
import tmg.flashback.formula1.extensions.getConstructorInProgressInfo
import tmg.flashback.formula1.extensions.getDriverInProgressInfo
import tmg.flashback.formula1.model.*
import tmg.flashback.statistics.R
import tmg.flashback.statistics.controllers.HomeController
import tmg.flashback.statistics.extensions.analyticsLabel
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.statistics.repo.SeasonRepository
import tmg.flashback.statistics.repo.repository.CacheRepository
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable.*
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

//region Inputs

interface SeasonViewModelInputs {
    fun clickMenu()
    fun clickItem(item: SeasonNavItem)

    fun selectSeason(season: Int)
    fun refresh()

    fun clickTrack(track: SeasonItem.Track)
    fun clickDriver(driver: SeasonItem.Driver)
    fun clickConstructor(constructor: SeasonItem.Constructor)
}

//endregion

//region Outputs

interface SeasonViewModelOutputs {
    val openMenu: LiveData<Event>
    val label: LiveData<String>

    val list: LiveData<List<SeasonItem>>
    val showLoading: LiveData<Boolean>

    val openRace: LiveData<DataEvent<SeasonItem.Track>>
    val openDriver: LiveData<DataEvent<SeasonItem.Driver>>
    val openConstructor: LiveData<DataEvent<SeasonItem.Constructor>>
}

//endregion

class SeasonViewModel(
    private val homeController: HomeController,
    private val raceRepository: RaceRepository,
    private val networkConnectivityManager: NetworkConnectivityManager,
    private val overviewRepository: OverviewRepository,
    private val seasonRepository: SeasonRepository,
    private val analyticsManager: AnalyticsManager,
    private val themeController: ThemeController,
    private val cacheRepository: CacheRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), SeasonViewModelInputs, SeasonViewModelOutputs {

    var inputs: SeasonViewModelInputs = this
    var outputs: SeasonViewModelOutputs = this

    private val menuItem: MutableStateFlow<SeasonNavItem> = MutableStateFlow(SeasonNavItem.SCHEDULE)
    private val season: MutableStateFlow<Int> = MutableStateFlow(homeController.defaultSeason)
    private val seasonWithRequest: Flow<Int?> = season
        .flatMapLatest { season ->
            return@flatMapLatest flow {
                if (raceRepository.shouldSyncRace(season)) {
                    showLoading.postValue(true)
                    emit(null)
                    val result = overviewRepository.fetchOverview(season)
                    val anotherResult = seasonRepository.fetchRaces(season)
                    showLoading.postValue(false)

                    emit(season)
                }
                else {
                    emit(season)
                }
            }
        }
        .flowOn(ioDispatcher)

    private val isConnected: Boolean
        get() = networkConnectivityManager.isConnected

    override val label: LiveData<String> = season
        .map { it.toString() }
        .asLiveData(viewModelScope.coroutineContext)

    override val openRace: MutableLiveData<DataEvent<SeasonItem.Track>> = MutableLiveData()
    override val openDriver: MutableLiveData<DataEvent<SeasonItem.Driver>> = MutableLiveData()
    override val openConstructor: MutableLiveData<DataEvent<SeasonItem.Constructor>> = MutableLiveData()

    override val openMenu: MutableLiveData<Event> = MutableLiveData()
    override val list: LiveData<List<SeasonItem>> = combine(seasonWithRequest, menuItem) { season, menuItem -> Pair(season, menuItem) }
        .flatMapLatest { (season, menuItem) ->

            analyticsManager.logEvent(menuItem.analyticsLabel, mapOf(
                "season" to season.toString()
            ))

            if (season == null) {
                return@flatMapLatest flow {
                    emit(listOf<SeasonItem>(SeasonItem.ErrorItem(SyncDataItem.Skeleton)))
                }
            }

            return@flatMapLatest when (menuItem) {
                SeasonNavItem.SCHEDULE -> getScheduleView(season, false)
                SeasonNavItem.CALENDAR -> getScheduleView(season, true)
                SeasonNavItem.DRIVERS -> getDriverStandings(season)
                SeasonNavItem.CONSTRUCTORS -> getConstructorStandings(season)
            }
        }
        .asLiveData(viewModelScope.coroutineContext)

    override val showLoading: MutableLiveData<Boolean> = MutableLiveData()

    init {
        if (cacheRepository.shouldSyncCurrentSeason()) {
            refresh(homeController.serverDefaultSeason)
        }
    }

    //region Inputs

    override fun clickMenu() {
        openMenu.value = Event()
    }

    override fun clickItem(item: SeasonNavItem) {
        menuItem.value = item
    }

    override fun selectSeason(season: Int) {
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

    override fun refresh() {
        this.refresh(this.season.value)
    }
    private fun refresh(season: Int) {
        viewModelScope.launch(context = ioDispatcher) {
            val result = overviewRepository.fetchOverview(season)
            val anotherResult = seasonRepository.fetchRaces(season)

            if (season == homeController.serverDefaultSeason) {
                cacheRepository.markedCurrentSeasonSynchronised()
            }
            showLoading.postValue(false)
        }
    }

    private fun getScheduleView(season: Int, isCalendarView: Boolean): Flow<List<SeasonItem>> {
        return overviewRepository.getOverview(season)
            .map {
                val list = getBannerList()
                when {
                    it.overviewRaces.isEmpty() && !isConnected -> list.addError(SyncDataItem.PullRefresh)
                    it.overviewRaces.isEmpty() && season == currentSeasonYear -> list.addError(SyncDataItem.Unavailable(SEASON_EARLY))
                    it.overviewRaces.isEmpty() && season > currentSeasonYear -> list.addError(SyncDataItem.Unavailable(SEASON_IN_FUTURE))
                    it.overviewRaces.isEmpty() -> list.addError(SyncDataItem.Unavailable(SEASON_INTERNAL_ERROR))
                    isCalendarView -> list.addAll(it.overviewRaces.toCalendar(season))
                    else -> list.addAll(it.overviewRaces.toScheduleList())
                }

                return@map list
            }
    }

    private fun getDriverStandings(season: Int): Flow<List<SeasonItem>> {
        return seasonRepository.getDriverStandings(season)
            .map {
                val list = getBannerList()
                when {
                    (it == null || it.standings.isEmpty()) && !isConnected -> list.addError(SyncDataItem.PullRefresh)
                    (it == null || it.standings.isEmpty()) && season >= currentSeasonYear -> list.addError(SyncDataItem.Unavailable(STANDINGS_EARLY))
                    (it == null || it.standings.isEmpty()) -> list.addError(SyncDataItem.Unavailable(STANDINGS_INTERNAL_ERROR))
                    else -> {
                        val inProgressInfo = it.standings.getDriverInProgressInfo()
                        if (inProgressInfo != null) {
                            list.addError(SyncDataItem.MessageRes(R.string.results_accurate_for, listOf(inProgressInfo.first, inProgressInfo.second)))
                        }
                        list.addAll(it.toDriverList())
                    }
                }

                return@map list
            }
    }

    private fun getConstructorStandings(season: Int): Flow<List<SeasonItem>> {
        return seasonRepository.getConstructorStandings(season)
            .map {
                val list = getBannerList()
                when {
                    (it == null || it.standings.isEmpty()) && !isConnected -> list.addError(SyncDataItem.PullRefresh)
                    (it == null || it.standings.isEmpty()) && season >= currentSeasonYear -> list.addError(SyncDataItem.Unavailable(STANDINGS_EARLY))
                    (it == null || it.standings.isEmpty()) -> list.addError(SyncDataItem.Unavailable(STANDINGS_INTERNAL_ERROR))
                    else -> {
                        val inProgressInfo = it.standings.getConstructorInProgressInfo()
                        if (inProgressInfo != null) {
                            list.addError(SyncDataItem.MessageRes(R.string.results_accurate_for, listOf(inProgressInfo.first, inProgressInfo.second)))
                        }
                        list.addAll(it.toConstructorList())
                    }
                }

                return@map list
            }
    }



    /**
     * Convert OverviewRace to a list of season items
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
                    hasResults = it.hasResults,
                    defaultExpanded = it.round == this.getDefaultExpandedRound(),
                    schedule = it.schedule
                )
            }
    }
    private fun List<OverviewRace>.getDefaultExpandedRound(): Int? {
        return this
            .filter { it.date >= LocalDate.now() }
            .minByOrNull { it.date }
            ?.round
    }

    /**
     * Convert OverviewRace to a list of calendar items
     */
    private fun List<OverviewRace>.toCalendar(season: Int): List<SeasonItem> {
        val list = mutableListOf<SeasonItem>()
        list.add(SeasonItem.CalendarHeader)
        Month.values().forEach { month ->
            var start = LocalDate.of(season, month.value, 1)
            var end = when (start.dayOfWeek) {
                DayOfWeek.SUNDAY -> {
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
     * Convert the driver standings construct into a list of home items to display on the home page
     * Compatibility function
     */
    private fun SeasonDriverStandings.toDriverList(): List<SeasonItem> {
        return this
            .standings
            .sortedBy { it.championshipPosition }
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
            .sortedBy { it.championshipPosition }
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

    //region Helpers

    private fun getBannerList(): MutableList<SeasonItem> {
        val list = mutableListOf<SeasonItem>()
        homeController.banner?.let {
            list.addError(SyncDataItem.Message(it.message, it.url))
        }
        return list
    }

    //endregion
}

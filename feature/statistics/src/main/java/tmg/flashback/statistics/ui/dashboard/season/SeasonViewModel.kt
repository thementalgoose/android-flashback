package tmg.flashback.statistics.ui.dashboard.season

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.TemporalAdjusters
import tmg.core.analytics.manager.AnalyticsManager
import tmg.flashback.formula1.constants.Formula1.constructorChampionshipStarts
import androidx.lifecycle.ViewModel
import tmg.core.device.controllers.DeviceController
import tmg.core.device.managers.NetworkConnectivityManager
import tmg.flashback.data.db.stats.HistoryRepository
import tmg.flashback.data.db.stats.SeasonOverviewRepository
import tmg.flashback.data.models.stats.*
import tmg.core.ui.controllers.ThemeController
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.statistics.R
import tmg.flashback.formula1.constants.Formula1.currentSeasonYear
import tmg.flashback.statistics.controllers.SeasonController
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

    fun showUpNext(value: Boolean = true)
    fun refresh()
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

    val showLoading: LiveData<Boolean>
    val label: LiveData<StringHolder>
    val list: LiveData<List<SeasonItem>>
}

//endregion

class SeasonViewModel(
        private val themeController: ThemeController,
        private val historyRepository: HistoryRepository,
        private val seasonOverviewRepository: SeasonOverviewRepository,
        private val seasonController: SeasonController,
        private val networkConnectivityManager: NetworkConnectivityManager,
        private val analyticsManager: AnalyticsManager
): ViewModel(), SeasonViewModelInputs, SeasonViewModelOutputs {

    private val currentTab: ConflatedBroadcastChannel<SeasonNavItem> =
        ConflatedBroadcastChannel(SeasonNavItem.SCHEDULE)
    private val currentTabFlow: Flow<SeasonNavItem> = currentTab.asFlow()
    private val season: ConflatedBroadcastChannel<Int> = ConflatedBroadcastChannel(seasonController.defaultSeason)
    private val currentHistory: Flow<History?> = season.asFlow()
        .flatMapLatest { historyRepository.historyFor(it) }

    override val openRace: MutableLiveData<DataEvent<SeasonItem.Track>> = MutableLiveData()
    override val openDriver: MutableLiveData<DataEvent<SeasonItem.Driver>> = MutableLiveData()
    override val openConstructor: MutableLiveData<DataEvent<SeasonItem.Constructor>> = MutableLiveData()

    override val showLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    override val openMenu: MutableLiveData<Event> = MutableLiveData()
    override val openNow: MutableLiveData<Event> = MutableLiveData()
    override val showUpNext: MutableLiveData<Boolean> = MutableLiveData(false)

    /**
     * Label to be shown at the top of the screen to indicate what year it is
     */
    override val label: LiveData<StringHolder> = season.asFlow()
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
        .asFlow()
        .flatMapLatest {
            seasonOverviewRepository.getSeasonOverview(it).combinePair(currentHistory)
        }
        .combinePair(
            currentTabFlow,
        )
        .map { (seasonAndHistory, menuItemType) ->
            val (season, history) = seasonAndHistory

            when (menuItemType) {
                SeasonNavItem.CALENDAR -> analyticsManager.logEvent("select_dashboard_calendar", mapOf(
                    "season" to season.season.toString()
                ))
                SeasonNavItem.SCHEDULE -> analyticsManager.logEvent("select_dashboard_schedule", mapOf(
                    "season" to season.season.toString()
                ))
                SeasonNavItem.DRIVERS -> analyticsManager.logEvent("select_dashboard_driver", mapOf(
                    "season" to season.season.toString()
                ))
                SeasonNavItem.CONSTRUCTORS -> analyticsManager.logEvent("select_dashboard_constructor", mapOf(
                    "season" to season.season.toString()
                ))
            }

            val appBannerMessage = seasonController.banner
            val list: MutableList<SeasonItem> = mutableListOf()
            val historyRounds = history?.rounds ?: emptyList()
            val rounds = season.rounds

            appBannerMessage?.let {
                if (it.isNotEmpty()) {
                    list.addError(SyncDataItem.Message(it))
                }
            }

            when (menuItemType) {
                SeasonNavItem.CALENDAR -> {
                    when {
                        historyRounds.isEmpty() && !networkConnectivityManager.isConnected ->
                            list.addError(SyncDataItem.NoNetwork)
                        historyRounds.isEmpty() && season.season == currentSeasonYear ->
                            list.addError(SyncDataItem.Unavailable(DataUnavailable.EARLY_IN_SEASON))
                        historyRounds.isEmpty() ->
                            list.addError(SyncDataItem.Unavailable(DataUnavailable.MISSING_RACE))
                        else ->
                            list.addAll(historyRounds.toCalendar(season.season))
                    }
                }
                SeasonNavItem.SCHEDULE -> {
                    when {
                        historyRounds.isEmpty() && !networkConnectivityManager.isConnected ->
                            list.addError(SyncDataItem.NoNetwork)
                        historyRounds.isEmpty() && season.season == currentSeasonYear ->
                            list.addError(SyncDataItem.Unavailable(DataUnavailable.EARLY_IN_SEASON))
                        historyRounds.isEmpty() ->
                            list.addError(SyncDataItem.Unavailable(DataUnavailable.MISSING_RACE))
                        else ->
                            list.addAll(historyRounds.toScheduleList())
                    }
                }
                SeasonNavItem.DRIVERS -> {
                    when {
                        rounds.isEmpty() && !networkConnectivityManager.isConnected ->
                            list.addError(SyncDataItem.NoNetwork)
                        rounds.isEmpty() ->
                            list.addError(SyncDataItem.Unavailable(DataUnavailable.IN_FUTURE_SEASON))
                        else -> {
                            val maxRound = rounds
                                .filter { it.race.isNotEmpty() }
                                .maxByOrNull { it.round }
                            if (maxRound != null && historyRounds.size != rounds.size) {
                                list.addError(SyncDataItem.MessageRes(R.string.results_accurate_for, listOf(maxRound.name, maxRound.round)))
                            }
                            when {
                                season.driverStandings.isEmpty() -> {
                                    val driverStandings = rounds.driverStandings()
                                    list.addAll(driverStandings.toDriverList(rounds))
                                }
                                else -> {
                                    list.addAll(season.driverStandings.toDriverList(rounds))
                                }
                            }
                        }
                    }
                }
                SeasonNavItem.CONSTRUCTORS -> {
                    when {
                        season.season < constructorChampionshipStarts ->
                            list.addError(SyncDataItem.ConstructorsChampionshipNotAwarded)
                        rounds.isEmpty() && !networkConnectivityManager.isConnected ->
                            list.addError(SyncDataItem.NoNetwork)
                        rounds.isEmpty() ->
                            list.addError(SyncDataItem.Unavailable(DataUnavailable.IN_FUTURE_SEASON))
                        else -> {
                            val maxRound = rounds
                                .filter { it.race.isNotEmpty() }
                                .maxByOrNull { it.round }
                            if (maxRound != null && historyRounds.size != rounds.size) {
                                list.addError(SyncDataItem.MessageRes(R.string.results_accurate_for, listOf(maxRound.name, maxRound.round)))
                            }
                            val constructorStandings = season.constructorStandings()
                            list.addAll(constructorStandings.toConstructorList(season))
                        }
                    }
                }
            }

            return@map list
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
        if (item != currentTab.valueOrNull) {
            showLoading.value = true
            currentTab.offer(item)
        }
    }

    override fun refresh() {
        this.season.valueOrNull?.let {
            this.season.offer(it)
        }
    }

    override fun selectSeason(season: Int) {
        showLoading.value = true
        this.season.offer(season)
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

    /**
     * Extract the calendar of events out into a formatted display list
     */
    private fun List<HistoryRound>.toCalendar(season: Int): List<SeasonItem> {
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
    private fun List<HistoryRound>.toScheduleList(): List<SeasonItem> {
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
     */
    private fun List<SeasonStanding<Driver>>.toDriverList(rounds: List<Round>): List<SeasonItem> {
        return this
            .sortedBy { it.position }
            .toList()
            .mapIndexed { index: Int, standing: SeasonStanding<Driver> ->
                SeasonItem.Driver(
                    season = season.value,
                    driver = standing.item,
                    points = standing.points,
                    position = index + 1,
                    bestQualifying = rounds.bestQualifyingResultFor(standing.item.id),
                    bestFinish = rounds.bestRaceResultFor(standing.item.id),
                    maxPointsInSeason = this.maxByOrNull { it.points }?.points ?: 0,
                    animationSpeed = themeController.animationSpeed
                )
            }
    }
    /**
     * Convert the driver standings construct into a list of home items to display on the home page
     * Compatibility function
     */
    @Deprecated("Should not be required if the standings model is available. Data migration in progress", replaceWith = ReplaceWith("List<SeasonStanding<Driver>>.toDriverList(rounds: List<Round>)"))
    private fun DriverStandingsRound.toDriverList(rounds: List<Round>): List<SeasonItem> {
        return this
            .values
            .sortedByDescending { it.second }
            .toList()
            .mapIndexed { index: Int, pair: Pair<RoundDriver, Int> ->
                val (roundDriver, points) = pair
                SeasonItem.Driver(
                    season = season.value,
                    driver = roundDriver.toDriver(),
                    points = points,
                    position = index + 1,
                    bestQualifying = rounds.bestQualifyingResultFor(roundDriver.id),
                    bestFinish = rounds.bestRaceResultFor(roundDriver.id),
                    maxPointsInSeason = this.values.maxByOrNull { it.second }?.second ?: 0,
                    animationSpeed = themeController.animationSpeed
                )
            }
    }

    /**
     * Convert the constructor standings construct into a list of home items to display on the home page
     */
    private fun ConstructorStandingsRound.toConstructorList(season: Season): List<SeasonItem> {
        return this
            .values
            .toList()
            .mapIndexed { index: Int, triple: Triple<Constructor, Map<String, Pair<Driver, Int>>, Int> ->
                val (constructor, driverPoints, constructorPoints) = triple
                SeasonItem.Constructor(
                    season = season.season,
                    position = index + 1,
                    constructor = constructor,
                    driver = driverPoints.values.sortedByDescending { it.second },
                    points = season.constructorStandings.firstOrNull { it.item.id == constructor.id }?.points ?: constructorPoints,
                    maxPointsInSeason = this.maxConstructorPointsInSeason(),
                    barAnimation = themeController.animationSpeed
                )
            }
            .sortedByDescending { it.points }
    }
}

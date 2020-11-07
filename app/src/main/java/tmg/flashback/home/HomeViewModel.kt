package tmg.flashback.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import tmg.flashback.R
import tmg.flashback.base.BaseViewModel
import tmg.flashback.constructorChampionshipStarts
import tmg.flashback.currentYear
import tmg.flashback.home.list.HomeItem
import tmg.flashback.home.list.addError
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.db.stats.DataDB
import tmg.flashback.repo.db.stats.HistoryDB
import tmg.flashback.repo.db.stats.SeasonOverviewDB
import tmg.flashback.repo.models.AppBanner
import tmg.flashback.repo.models.stats.*
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.shared.SyncDataItem
import tmg.flashback.shared.viewholders.DataUnavailable
import tmg.flashback.di.async.ScopeProvider
import tmg.flashback.di.device.BuildConfigProvider
import tmg.flashback.utils.StringHolder
import tmg.utilities.extensions.combinePair
import tmg.utilities.extensions.combineTriple
import tmg.utilities.extensions.then
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

//region Inputs

interface HomeViewModelInputs {
    fun clickItem(item: HomeMenuItem)
    fun selectSeason(season: Int)
}

//endregion

//region Outputs

interface HomeViewModelOutputs {
    val list: LiveData<List<HomeItem>>
    val openSeasonList: MutableLiveData<DataEvent<Boolean>>
    val label: LiveData<StringHolder>
    val showLoading: MutableLiveData<Boolean>
    val ensureOnCalendar: MutableLiveData<Event>
    val openAppLockout: LiveData<Event>
    val openReleaseNotes: MutableLiveData<Event>
}

//endregion

@Suppress("EXPERIMENTAL_API_USAGE")
class HomeViewModel(
    private val seasonOverviewDB: SeasonOverviewDB,
    private val historyDB: HistoryDB,
    dataDB: DataDB,
    private val prefDB: PrefsDB,
    private val connectivityManager: ConnectivityManager,
    private val buildConfigProvider: BuildConfigProvider,
    scopeProvider: ScopeProvider
) : BaseViewModel(scopeProvider), HomeViewModelInputs, HomeViewModelOutputs {

    // true = new season has been requested so don't progress
    private var invalidSeasonData: Boolean = true

    private val currentTab: ConflatedBroadcastChannel<HomeMenuItem> =
        ConflatedBroadcastChannel()
    private val currentTabFlow: Flow<HomeMenuItem> = currentTab.asFlow()
    private val appBanner: Flow<AppBanner?> = dataDB.appBanner()
    private var _season: Int = currentYear
    private val season: ConflatedBroadcastChannel<Int> = ConflatedBroadcastChannel()
    private val currentHistory: Flow<History> = season.asFlow()
            .flatMapLatest { historyDB.historyFor(it) }
            .filterNotNull()

    override val ensureOnCalendar: MutableLiveData<Event> = MutableLiveData()
    override val showLoading: MutableLiveData<Boolean> = MutableLiveData()
    override val openSeasonList: MutableLiveData<DataEvent<Boolean>> = MutableLiveData()

    override val label: LiveData<StringHolder> = season.asFlow()
        .map { season ->
            StringHolder(msg = season.toString())
        }
        .asLiveData(scope.coroutineContext)

    /**
     * List to handle season data
     * - CALENDAR
     * - DRIVERS
     * - CONSTRUCTORS
     */
    private val seasonList: Flow<List<HomeItem>> = season
        .asFlow()
        .flatMapLatest { seasonOverviewDB.getSeasonOverview(it) }
        .combineTriple(
            currentTabFlow,
            currentHistory
        )
        .combinePair(appBanner)
        .map { (seasonRoundMenuItemListHistoryTriple, appBanner) ->
            val (season, menuItemType, history) = seasonRoundMenuItemListHistoryTriple
            val list: MutableList<HomeItem> = mutableListOf()
            val historyRounds = history.rounds

            val rounds = season.rounds

            appBanner?.let {
                if (it.show && !it.message.isNullOrEmpty()) {
                    list.addError(SyncDataItem.Message(it.message ?: ""))
                }
            }

            when (menuItemType) {
                HomeMenuItem.CALENDAR -> {
                    when {
                        historyRounds.isEmpty() && !connectivityManager.isConnected ->
                            list.addError(SyncDataItem.NoNetwork)
                        historyRounds.isEmpty() && season.season == currentYear ->
                            list.addError(SyncDataItem.Unavailable(DataUnavailable.EARLY_IN_SEASON))
                        historyRounds.isEmpty() ->
                            list.addError(SyncDataItem.Unavailable(DataUnavailable.MISSING_RACE))
                        else ->
                            list.addAll(historyRounds.toCalendarList())
                    }
                }
                HomeMenuItem.DRIVERS -> {
                    when {
                        rounds.isEmpty() && !connectivityManager.isConnected ->
                            list.addError(SyncDataItem.NoNetwork)
                        rounds.isEmpty() ->
                            list.addError(SyncDataItem.Unavailable(DataUnavailable.IN_FUTURE_SEASON))
                        else -> {
                            val maxRound = rounds
                                    .filter { it.race.isNotEmpty() }
                                    .maxBy { it.round }
                            if (maxRound != null && historyRounds.size != rounds.size) {
                                list.addError(SyncDataItem.MessageRes(R.string.results_accurate_for, listOf(maxRound.name, maxRound.round)))
                            }
                            val driverStandings = rounds.driverStandings()
                            list.addAll(driverStandings.toDriverList(rounds))
                        }
                    }
                }
                HomeMenuItem.CONSTRUCTORS -> {
                    when {
                        season.season < constructorChampionshipStarts ->
                            list.addError(SyncDataItem.ConstructorsChampionshipNotAwarded)
                        rounds.isEmpty() && !connectivityManager.isConnected ->
                            list.addError(SyncDataItem.NoNetwork)
                        rounds.isEmpty() ->
                            list.addError(SyncDataItem.Unavailable(DataUnavailable.IN_FUTURE_SEASON))
                        else -> {
                            val maxRound = rounds
                                    .filter { it.race.isNotEmpty() }
                                    .maxBy { it.round }
                            if (maxRound != null && historyRounds.size != rounds.size) {
                                list.addError(SyncDataItem.MessageRes(R.string.results_accurate_for, listOf(maxRound.name, maxRound.round)))
                            }
                            val constructorStandings = season.constructorStandings()
                            list.addAll(constructorStandings.toConstructorList())
                        }
                    }
                }
                else -> {
                }
            }
            invalidSeasonData = false
            return@map list
        }
        .onStart { emitAll(flow { emptyList<HomeItem>() }) }

    /**
     * Overview list that gets returned to the Activity
     */
    override val list: LiveData<List<HomeItem>> = seasonList
        .then {
            showLoading.value = false
        }
        .asLiveData(scope.coroutineContext)

    //region App lockout and banner

    override val openAppLockout: LiveData<Event> = dataDB
        .appLockout()
        .map {
            if (it?.show == true && buildConfigProvider.shouldLockoutBasedOnVersion(it.version)) {
                Event()
            } else {
                null
            }
        }
        .filterNotNull()
        .asLiveData(scope.coroutineContext)
    override val openReleaseNotes: MutableLiveData<Event> = MutableLiveData()

    //endregion

    var inputs: HomeViewModelInputs = this
    var outputs: HomeViewModelOutputs = this

    init {
        season.offer(currentYear)
        currentTab.offer(HomeMenuItem.CALENDAR)
        showLoading.value = true

        if (prefDB.shouldShowReleaseNotes) {
            openReleaseNotes.value = Event()
        }
    }

    //region Inputs

    override fun clickItem(item: HomeMenuItem) {
        if (item != currentTab.value) {
            if (item == HomeMenuItem.SEASONS) {
                openSeasonList.value = DataEvent(prefDB.showBottomSheetExpanded)
            } else {
                showLoading.value = true
                currentTab.offer(item)
            }
        }
    }

    override fun selectSeason(season: Int) {
        invalidSeasonData = true
        showLoading.value = true
        _season = season
        this.season.offer(season)
    }

    //endregion


    /**
     * Extract the calendar of tracks out into a list of home items to display on the home screen
     */
    private fun List<HistoryRound>.toCalendarList(): List<HomeItem> {
        return this
            .sortedBy { it.round }
            .map {
                HomeItem.Track(
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
    private fun DriverStandingsRound.toDriverList(rounds: List<Round>): List<HomeItem> {
        return this
            .values
            .sortedByDescending { it.second }
            .toList()
            .mapIndexed { index: Int, pair: Pair<RoundDriver, Int> ->
                val (roundDriver, points) = pair
                HomeItem.Driver(
                    season = _season,
                    driver = roundDriver,
                    points = points,
                    position = index + 1,
                    bestQualifying = rounds.bestQualifyingResultFor(roundDriver.id),
                    bestFinish = rounds.bestRaceResultFor(roundDriver.id),
                    maxPointsInSeason = this.maxDriverPointsInSeason(),
                    barAnimation = prefDB.barAnimation
                )
            }
    }

    /**
     * Convert the constructor standings construct into a list of home items to display on the home page
     */
    private fun ConstructorStandingsRound.toConstructorList(): List<HomeItem> {
        return this
            .values
            .toList()
            .mapIndexed { index: Int, triple: Triple<Constructor, Map<String, Pair<Driver, Int>>, Int> ->
                val (constructor, driverPoints, constructorPoints) = triple
                HomeItem.Constructor(
                    season = _season,
                    position = index + 1,
                    constructor = constructor,
                    driver = driverPoints.values.sortedByDescending { it.second },
                    points = constructorPoints,
                    maxPointsInSeason = this.maxConstructorPointsInSeason(),
                    barAnimation = prefDB.barAnimation
                )
            }
            .sortedByDescending { it.points }
    }
}
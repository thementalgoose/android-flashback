package tmg.flashback.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import tmg.flashback.R
import tmg.flashback.base.BaseViewModel
import tmg.flashback.currentYear
import tmg.flashback.home.list.HomeItem
import tmg.flashback.home.list.addError
import tmg.flashback.isValidVersion
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.db.stats.DataDB
import tmg.flashback.repo.db.stats.HistoryDB
import tmg.flashback.repo.db.stats.SeasonOverviewDB
import tmg.flashback.repo.models.AppBanner
import tmg.flashback.repo.models.stats.*
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.shared.SyncDataItem
import tmg.flashback.shared.viewholders.DataUnavailable
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
    val label: LiveData<String>
    val currentSeason: LiveData<Int>

    val showLoading: MutableLiveData<Boolean>

    val ensureOnCalendar: MutableLiveData<Event>

    val openCalendarFilter: LiveData<Boolean>
    val openAppLockout: LiveData<Event>

    val openReleaseNotes: MutableLiveData<Event>
}

//endregion

@FlowPreview
@ExperimentalCoroutinesApi
class HomeViewModel(
    private val context: Context,
    private val seasonOverviewDB: SeasonOverviewDB,
    private val historyDB: HistoryDB,
    private val dataDB: DataDB,
    private val prefDB: PrefsDB,
    private val connectivityManager: ConnectivityManager
) : BaseViewModel(), HomeViewModelInputs, HomeViewModelOutputs {

    // true = new season has been requested so don't progress
    private var invalidSeasonData: Boolean = true

    private val currentTab: ConflatedBroadcastChannel<HomeMenuItem> =
        ConflatedBroadcastChannel(HomeMenuItem.CALENDAR)
    private val currentTabFlow: Flow<HomeMenuItem> = currentTab.asFlow()
    private val appBanner: Flow<AppBanner?> = dataDB.appBanner()
    private var _season: Int = currentYear
    private val season: ConflatedBroadcastChannel<Int> = ConflatedBroadcastChannel(currentYear)
    private val currentHistory: Flow<History> = season.asFlow()
            .flatMapLatest { historyDB.historyFor(it) }
            .filterNotNull()

    override val ensureOnCalendar: MutableLiveData<Event> = MutableLiveData()
    override val showLoading: MutableLiveData<Boolean> = MutableLiveData(true)

    override val currentSeason: LiveData<Int> = season
        .asFlow()
        .asLiveData(viewModelScope.coroutineContext)

    override val openSeasonList: MutableLiveData<DataEvent<Boolean>> = MutableLiveData()
    override val openCalendarFilter: LiveData<Boolean> = currentTab.asFlow()
        .map {
            return@map when (it) {
                HomeMenuItem.CALENDAR -> true
                HomeMenuItem.DRIVERS -> true
                HomeMenuItem.CONSTRUCTORS -> true
                HomeMenuItem.SEASONS -> true
                HomeMenuItem.NEWS -> false
                HomeMenuItem.SEARCH -> false
            }
        }
        .asLiveData(viewModelScope.coroutineContext)

    override val label: LiveData<String> = currentTabFlow
        .combinePair(season.asFlow())
        .map { (currentTab, season) ->
            return@map when (currentTab) {
                HomeMenuItem.CALENDAR -> season.toString()
                HomeMenuItem.DRIVERS -> season.toString()
                HomeMenuItem.CONSTRUCTORS -> season.toString()
                HomeMenuItem.SEASONS -> season.toString()
                HomeMenuItem.NEWS -> context.getString(R.string.nav_news)
                HomeMenuItem.SEARCH -> ""
            }
        }
        .asLiveData(viewModelScope.coroutineContext)

    /**
     * List to handle season data
     * - CALENDAR
     * - DRIVERS
     * - CONSTRUCTORS
     */
    private val seasonList: Flow<List<HomeItem>> = season
        .asFlow()
        .flatMapLatest { seasonOverviewDB.getSeasonOverview(it) }
            .then {
                println("Triggered here! number 1")
            }
        .combineTriple(
            currentTabFlow,
            currentHistory
        )
        .then {
            println("Triggered here!")
        }
        .combinePair(appBanner)
        .map { (seasonRoundMenuItemListHistoryTriple, appBanner) ->
            val (seasonAndRounds, menuItemType, history) = seasonRoundMenuItemListHistoryTriple
            val (season, rounds) = seasonAndRounds
            val list: MutableList<HomeItem> = mutableListOf()
            val historyRounds = history.rounds

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
                        historyRounds.isEmpty() && season == currentYear ->
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
                                list.addError(SyncDataItem.Message(context.getString(R.string.results_accurate_for, maxRound.name, maxRound.round)))
                            }
                            val driverStandings = rounds.driverStandings()
                            list.addAll(driverStandings.toDriverList(rounds))
                        }
                    }
                }
                HomeMenuItem.CONSTRUCTORS -> {
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
                                list.addError(SyncDataItem.Message(context.getString(R.string.results_accurate_for, maxRound.name, maxRound.round)))
                            }
                            val constructorStandings = rounds.constructorStandings()
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
        .asLiveData(viewModelScope.coroutineContext)

    //region App lockout and banner

    override val openAppLockout: LiveData<Event> = dataDB
        .appLockout()
        .map {
            if (it?.show == true && isValidVersion(it.version)) {
                Event()
            } else {
                null
            }
        }
        .filterNotNull()
        .asLiveData(viewModelScope.coroutineContext)
    override val openReleaseNotes: MutableLiveData<Event> = MutableLiveData()

    //endregion

    var inputs: HomeViewModelInputs = this
    var outputs: HomeViewModelOutputs = this

    init {
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
    private fun DriverStandings.toDriverList(rounds: List<Round>): List<HomeItem> {
        return this
            .values
            .sortedByDescending { it.second }
            .toList()
            .mapIndexed { index: Int, pair: Pair<RoundDriver, Int> ->
                val (roundDriver, points) = pair
                HomeItem.Driver(
                    season = _season,
                    driver = roundDriver.toDriver(),
                    points = points,
                    position = index + 1,
                    bestQualifying = rounds.bestQualifyingResultFor(roundDriver.id),
                    bestFinish = rounds.bestRaceResultFor(roundDriver.id),
                    maxPointsInSeason = this.maxDriverPointsInSeason()
                )
            }
    }

    /**
     * Convert the constructor standings construct into a list of home items to display on the home page
     */
    private fun ConstructorStandings.toConstructorList(): List<HomeItem> {
        return this
            .values
            .sortedByDescending { it.second.allPoints() }
            .toList()
            .mapIndexed { index: Int, pair: Pair<Constructor, Map<String, Pair<Driver, Int>>> ->
                val (constructor, driverPoints) = pair
                HomeItem.Constructor(
                    season = _season,
                    position = index + 1,
                    constructor = constructor,
                    driver = driverPoints.values.sortedByDescending { it.second },
                    points = driverPoints.allPoints(),
                    maxPointsInSeason = this.maxConstructorPointsInSeason()
                )
            }
    }
}
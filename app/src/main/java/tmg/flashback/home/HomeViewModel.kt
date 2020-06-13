package tmg.flashback.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import tmg.flashback.base.BaseViewModel
import tmg.flashback.currentYear
import tmg.flashback.home.list.HomeItem
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.db.news.NewsDB
import tmg.flashback.repo.db.stats.DataDB
import tmg.flashback.repo.db.stats.HistoryDB
import tmg.flashback.repo.db.stats.SeasonOverviewDB
import tmg.flashback.repo.models.stats.*
import tmg.flashback.settings.ConnectivityManager
import tmg.utilities.extensions.combineTriple
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
    val openSeasonList: MutableLiveData<Event>
    val currentSeason: LiveData<Int>

    val openCalendarFilter: LiveData<Boolean>
    val openAppLockout: LiveData<Event>
    val openAppBanner: LiveData<String?>

    val openReleaseNotes: MutableLiveData<Event>
}

//endregion

@FlowPreview
@ExperimentalCoroutinesApi
class HomeViewModel(
    private val seasonOverviewDB: SeasonOverviewDB,
    private val historyDB: HistoryDB,
    private val dataDB: DataDB,
    private val prefDB: PrefsDB,
    private val newsDB: NewsDB,
    private val connectivityManager: ConnectivityManager
) : BaseViewModel(), HomeViewModelInputs, HomeViewModelOutputs {

    private val currentTab: ConflatedBroadcastChannel<HomeMenuItem> =
        ConflatedBroadcastChannel(HomeMenuItem.NEWS)
    private val season: ConflatedBroadcastChannel<Int> = ConflatedBroadcastChannel(currentYear)
    private val refreshNews: ConflatedBroadcastChannel<Boolean> = ConflatedBroadcastChannel(true)

    override val currentSeason: LiveData<Int> = season
        .asFlow()
        .asLiveData(viewModelScope.coroutineContext)

    override val openSeasonList: MutableLiveData<Event> = MutableLiveData()
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
            currentTab.asFlow(),
            historyDB.allHistory()
        ) // TODO: Convert this to use a summary document
        .map { (seasonAndRounds, menuItemType, historyList) ->
            val (season, rounds) = seasonAndRounds
            val list: MutableList<HomeItem> = mutableListOf()
            val history = historyList
                .firstOrNull { it.season == season }
            val historyRounds = history?.rounds ?: emptyList()
            when (menuItemType) {
                HomeMenuItem.CALENDAR -> {
                    list.addAll(historyRounds.toCalendarList())
                }
                HomeMenuItem.DRIVERS -> {
                    val driverStandings = rounds.driverStandings()
                    list.addAll(driverStandings.toDriverList())
                }
                HomeMenuItem.CONSTRUCTORS -> {
                    val constructorStandings = rounds.constructorStandings()
                    list.addAll(constructorStandings.toConstructorList())
                }
                else -> {
                }
            }
            return@map list
        }

    /**
     * List to handle news data
     */
    private val newsList: Flow<List<HomeItem>> = refreshNews
        .asFlow()
        .flatMapLatest { newsDB.getNews() }
        .map { response ->
            response.result?.map { HomeItem.NewsArticle(it) } ?: emptyList()
        }

    /**
     * Overview list that gets returned to the Activity
     */
    override val list: LiveData<List<HomeItem>> = currentTab
        .asFlow()
        .combineTriple(seasonList, newsList)
        .map { (tab, seasonList, newsList) ->
            return@map when (tab) {
                HomeMenuItem.NEWS -> newsList
                HomeMenuItem.CALENDAR,
                HomeMenuItem.DRIVERS,
                HomeMenuItem.CONSTRUCTORS -> seasonList
                else -> emptyList()
            }
        }
        .asLiveData(viewModelScope.coroutineContext)

    //region App lockout and banner

    override val openAppLockout: LiveData<Event> = dataDB
        .appLockout()
        .map {
            if (it?.show == true) {
                Event()
            } else {
                null
            }
        }
        .filterNotNull()
        .asLiveData(viewModelScope.coroutineContext)
    override val openAppBanner: LiveData<String?> = dataDB
        .appBanner()
        .map {
            if (it?.show == true) {
                it.message
            } else {
                null
            }
        }
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
        if (item == HomeMenuItem.SEASONS) {
            openSeasonList.value = Event()
        } else {
            currentTab.offer(item)
        }
    }

    override fun selectSeason(season: Int) {
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
                    circuitName = it.circuitName,
                    raceCountry = it.country,
                    raceCountryISO = it.countryISO,
                    date = it.date,
                    hasData = it.hasResults
                )
            }
    }

    /**
     * Convert the driver standings construct into a list of home items to display on the home page
     */
    private fun DriverStandings.toDriverList(): List<HomeItem> {
        return this
            .values
            .sortedByDescending { it.second }
            .toList()
            .mapIndexed { index: Int, pair: Pair<RoundDriver, Int> ->
                val (roundDriver, points) = pair
                HomeItem.Driver(
                    driver = roundDriver,
                    points = points,
                    position = index + 1,
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
                    position = index + 1,
                    constructor = constructor,
                    driver = driverPoints.values.sortedByDescending { it.second },
                    points = driverPoints.allPoints(),
                    maxPointsInSeason = this.maxConstructorPointsInSeason()
                )
            }
    }
}
package tmg.flashback.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import tmg.flashback.base.BaseViewModel
import tmg.flashback.currentYear
import tmg.flashback.home.list.HomeItem
import tmg.flashback.home.list.viewholders.NoDataViewHolder
import tmg.flashback.repo.db.DataDB
import tmg.flashback.repo.db.HistoryDB
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.db.SeasonOverviewDB
import tmg.flashback.repo.models.*
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.utils.SeasonRound
import tmg.utilities.extensions.combineTriple
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
    val openSeasonList: MutableLiveData<Event>
    val currentSeason: LiveData<Int>

    val openAppLockout: LiveData<Event>
    val openAppBanner: LiveData<String?>

    val openReleaseNotes: MutableLiveData<Event>
}

//endregion

class HomeViewModel(
    private val seasonOverviewDB: SeasonOverviewDB,
    private val historyDB: HistoryDB,
    private val dataDB: DataDB,
    private val prefDB: PrefsDB,
    private val connectivityManager: ConnectivityManager
) : BaseViewModel(), HomeViewModelInputs, HomeViewModelOutputs {

    private val currentTab: ConflatedBroadcastChannel<HomeMenuItem> =
        ConflatedBroadcastChannel(HomeMenuItem.CALENDAR)
    private val season: ConflatedBroadcastChannel<Int> = ConflatedBroadcastChannel(currentYear)
    override val currentSeason: LiveData<Int> = season
        .asFlow()
        .asLiveData(viewModelScope.coroutineContext)

    override val openSeasonList: MutableLiveData<Event> = MutableLiveData()
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

    override val list: LiveData<List<HomeItem>> = season
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
                    list.addAll(historyRounds
                        .sortedBy { it.round }
                        .map {
                            HomeItem.Track(
                                season = it.season,
                                round = it.round,
                                raceName = it.raceName,
                                circuitName = it.circuitName,
                                raceCountry = it.country,
                                raceCountryISO = it.countryISO,
                                date = it.date
                            )
                        })
                }
                HomeMenuItem.DRIVERS -> {
                    val driverStandings = rounds.standingsDriver()
                    list.addAll(driverStandings
                        .values
                        .sortedByDescending { it.second }
                        .toList()
                        .mapIndexed { index: Int, pair: Pair<RoundDriver, Int> ->
                            val (roundDriver, points) = pair
                            HomeItem.Driver(
                                driver = roundDriver,
                                points = points,
                                position = index + 1,
                                maxPointsInSeason = driverStandings.maxDriverPointsInSeason()
                            )
                        })
                }
                HomeMenuItem.CONSTRUCTORS -> {
                    val constructorStandings = rounds.standingsConstructor()
                    list.addAll(constructorStandings
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
                                maxPointsInSeason = constructorStandings.maxConstructorPointsInSeason()
                            )
                        })
                }
                else -> {
                }
            }
            if (list.isEmpty()) {
                if (!connectivityManager.isConnected) {
                    list.add(HomeItem.NoNetwork)
                }
                else {
                    if (history?.completed == 0 || history == null) {
                        list.add(HomeItem.NoData(false))
                    } else {
                        list.add(HomeItem.NoData(true))
                    }
                }
            }
            return@map list
        }
        .asLiveData(viewModelScope.coroutineContext)

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
}
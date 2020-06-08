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
}

//endregion

class HomeViewModel(
    private val seasonOverviewDB: SeasonOverviewDB,
    private val historyDB: HistoryDB,
    private val dataDB: DataDB,
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
            when (menuItemType) {
                HomeMenuItem.CALENDAR -> {
                    list.addAll((historyList
                        .firstOrNull { it.season == season }?.rounds ?: emptyList())
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
                if (connectivityManager.isConnected || season == currentYear) {
                    list.add(HomeItem.NoData)
                }
                else {
                    list.add(HomeItem.NoNetwork)
                }
            }
            return@map list
        }
        .asLiveData(viewModelScope.coroutineContext)

    var inputs: HomeViewModelInputs = this
    var outputs: HomeViewModelOutputs = this

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
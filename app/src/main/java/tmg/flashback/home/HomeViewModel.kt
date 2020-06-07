package tmg.flashback.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import tmg.flashback.base.BaseViewModel
import tmg.flashback.home.list.HomeItem
import tmg.flashback.repo.db.SeasonOverviewDB
import tmg.flashback.repo.models.*
import tmg.flashback.utils.SeasonRound
import tmg.utilities.extensions.combinePair
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

//region Inputs

interface HomeViewModelInputs {
    fun clickItem(item: HomeMenuItem)
    fun selectSeason(season: Int)
    fun clickTrack(season: Int, round: Int)
}

//endregion

//region Outputs

interface HomeViewModelOutputs {
    val list: LiveData<List<HomeItem>>
    val openSeasonList: MutableLiveData<Event>
    val openRace: MutableLiveData<DataEvent<SeasonRound>>
    val currentSeason: LiveData<Int>
}

//endregion

class HomeViewModel(
    private val seasonOverviewDB: SeasonOverviewDB
) : BaseViewModel(), HomeViewModelInputs, HomeViewModelOutputs {

    private val currentTab: ConflatedBroadcastChannel<HomeMenuItem> =
        ConflatedBroadcastChannel(HomeMenuItem.CALENDAR)
    private val season: ConflatedBroadcastChannel<Int> = ConflatedBroadcastChannel(2019)
    override val currentSeason: LiveData<Int> = season
        .asFlow()
        .asLiveData(viewModelScope.coroutineContext)

    override val openSeasonList: MutableLiveData<Event> = MutableLiveData()
    override val openRace: MutableLiveData<DataEvent<SeasonRound>> = MutableLiveData()

    override val list: LiveData<List<HomeItem>> = season
        .asFlow()
        .flatMapLatest { seasonOverviewDB.getSeasonOverview(it) }
        .combinePair(currentTab.asFlow())
        .map { (rounds, menuItemType) ->
            val list: MutableList<HomeItem> = mutableListOf()
            when (menuItemType) {
                HomeMenuItem.CALENDAR -> {
                    list.addAll(rounds
                        .sortedBy { it.round }
                        .map {
                            HomeItem.Track(
                                season = it.season,
                                round = it.round,
                                raceName = it.name,
                                circuitName = it.circuit.name,
                                raceCountry = it.circuit.country,
                                raceCountryISO = it.circuit.countryISO,
                                date = it.date,
                                time = it.time
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

    override fun clickTrack(season: Int, round: Int) {
        this.openRace.value = DataEvent(SeasonRound(season, round))
    }

    //endregion
}
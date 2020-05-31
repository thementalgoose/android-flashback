package tmg.flashback.standings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import tmg.flashback.base.BaseViewModel
import tmg.flashback.repo.db.HistoryDB
import tmg.flashback.repo.db.SeasonOverviewDB
import tmg.flashback.repo.models.*
import tmg.flashback.standings.StandingsTabType.CONSTRUCTOR
import tmg.flashback.standings.StandingsTabType.DRIVER
import tmg.utilities.extensions.combinePair
import tmg.utilities.extensions.combineTriple
import tmg.utilities.lifecycle.Event

//region Inputs

interface StandingsViewModelInputs {
    fun initForSeason(season: Int)
    fun clickBack()
    fun clickType(type: StandingsTabType)
}

//endregion

//region Outputs

interface StandingsViewModelOutputs {
    val list: LiveData<List<StandingsItem>>
    val goBack: MutableLiveData<Event>
}

//endregion

class StandingsViewModel(
    private val seasonOverviewDB: SeasonOverviewDB
): BaseViewModel(), StandingsViewModelInputs, StandingsViewModelOutputs {

    var inputs: StandingsViewModelInputs = this
    var outputs: StandingsViewModelOutputs = this

    private val typeEvent: ConflatedBroadcastChannel<StandingsTabType> = ConflatedBroadcastChannel(DRIVER)
    private val seasonEvent: ConflatedBroadcastChannel<Int> = ConflatedBroadcastChannel()

    override val goBack: MutableLiveData<Event> = MutableLiveData()
    override val list: LiveData<List<StandingsItem>> = seasonEvent
        .asFlow()
        .flatMapLatest { seasonOverviewDB.getSeasonOverview(it) }
        .combineTriple(seasonEvent.asFlow(), typeEvent.asFlow())
        .map { (rounds, season, type) ->
            val list: MutableList<StandingsItem> = mutableListOf(StandingsItem.Header(season, rounds.upcoming, rounds.completed))
            when (type) {
                DRIVER -> {
                    list.addAll(rounds
                        .standingsDriver()
                        .values
                        .sortedByDescending { it.second }
                        .toList()
                        .map { (roundDriver, points) ->
                            StandingsItem.Driver(
                                driver = roundDriver,
                                points = points
                            )
                        })
                }
                CONSTRUCTOR -> {
                    list.addAll(rounds
                        .standingsConstructor()
                        .values
                        .sortedByDescending { it.second.allPoints() }
                        .toList()
                        .map { (constructor, driverPoints) ->
                            StandingsItem.Constructor(
                                constructor = constructor,
                                driver = driverPoints.values.sortedByDescending { it.second },
                                points = driverPoints.allPoints()
                            )
                        })
                }
            }
            return@map list
        }
        .asLiveData(viewModelScope.coroutineContext)

    //region Inputs

    override fun initForSeason(season: Int) {
        seasonEvent.offer(season)
    }

    override fun clickBack() {
        goBack.value = Event()
    }

    override fun clickType(type: StandingsTabType) {
        typeEvent.offer(type)
    }

    //endregion
}
package tmg.flashback.standings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import tmg.flashback.base.BaseViewModel
import tmg.flashback.repo.db.HistoryDB
import tmg.flashback.repo.db.SeasonOverviewDB
import tmg.flashback.standings.StandingsTabType.DRIVER
import tmg.utilities.extensions.combinePair
import tmg.utilities.lifecycle.Event

//region Inputs

interface StandingsViewModelInputs {
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
        .combinePair(typeEvent.asFlow())
        .map { (rounds, type) ->

        }

    init {

    }

    //region Inputs

    override fun clickBack() {
        goBack.value = Event()
    }

    override fun clickType(type: StandingsTabType) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //endregion
}
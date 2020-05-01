package tmg.flashback.dashboard.season

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import tmg.flashback.base.BaseViewModel
import tmg.flashback.repo.db.HistoryDB
import tmg.flashback.utils.SeasonRound
import tmg.utilities.extensions.combinePair
import tmg.utilities.lifecycle.DataEvent

//region Inputs

interface DashboardSeasonViewModelInputs {
    fun load(season: Int)
    fun switchType(type: DashboardSeasonViewType)

    fun clickRace(seasonRound: SeasonRound)
}

//endregion

//region Outputs

interface DashboardSeasonViewModelOutputs {
    val list: LiveData<List<DashboardSeasonAdapterItem>>
    val goToRace: MutableLiveData<DataEvent<SeasonRound>>
}

//endregion

class DashboardSeasonViewModel(
    private val historyDB: HistoryDB
): BaseViewModel(), DashboardSeasonViewModelInputs, DashboardSeasonViewModelOutputs {

    var inputs: DashboardSeasonViewModelInputs = this
    var outputs: DashboardSeasonViewModelOutputs = this

    private var seasonEvent: ConflatedBroadcastChannel<Int> = ConflatedBroadcastChannel()

    override val list: LiveData<List<DashboardSeasonAdapterItem>> = historyDB.allHistory()
        .combinePair(seasonEvent.asFlow())
        .map { (list, season) -> list.first { it.season == season }}
        .map { list ->
            list.rounds.map {
                DashboardSeasonAdapterItem.Track(
                    it.season,
                    it.round,
                    "PLACEHOLDER", // TODO
                    it.circuitName,
                    it.country,
                    it.countryISO
                )
            }
        }
        .asLiveData(viewModelScope.coroutineContext)
    override val goToRace: MutableLiveData<DataEvent<SeasonRound>> = MutableLiveData()

    //region Inputs

    override fun load(season: Int) {
        seasonEvent.offer(season)
    }

    override fun switchType(type: DashboardSeasonViewType) {
        // TODO
    }

    override fun clickRace(seasonRound: SeasonRound) {
        goToRace.value = DataEvent(seasonRound)
    }

    //endregion

    //region Outputs

    //endregion
}
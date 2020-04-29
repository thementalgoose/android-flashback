package tmg.flashback.dashboard.season

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import tmg.flashback.base.BaseViewModel
import tmg.flashback.repo.db.HistoryDB
import tmg.utilities.extensions.combinePair

//region Inputs

interface DashboardSeasonViewModelInputs {
    fun load(season: Int)
    fun switchType(type: DashboardSeasonViewType)
}

//endregion

//region Outputs

interface DashboardSeasonViewModelOutputs {
    val list: LiveData<List<DashboardSeasonAdapterItem>>
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

    //region Inputs

    override fun load(season: Int) {
        seasonEvent.offer(season)
    }

    override fun switchType(type: DashboardSeasonViewType) {
        // TODO
    }

    //endregion

    //region Outputs

    //endregion
}
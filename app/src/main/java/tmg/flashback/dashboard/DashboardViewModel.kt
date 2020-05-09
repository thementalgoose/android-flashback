package tmg.flashback.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import tmg.flashback.base.BaseViewModel
import tmg.flashback.currentYear
import tmg.flashback.dashboard.swiping.season.DashboardSeasonAdapterItem
import tmg.flashback.dashboard.year.DashboardYearItem
import tmg.flashback.minimumSupportedYear
import tmg.flashback.repo.db.HistoryDB
import tmg.utilities.extensions.combinePair
import tmg.utilities.lifecycle.Event

//region Inputs

interface DashboardViewModelInputs {
    fun clickSeason(year: Int)
    fun clickSettings()
}

//endregion

//region Outputs

interface DashboardViewModelOutputs {
    val years: MutableLiveData<List<DashboardYearItem>>
    val seasonList: LiveData<List<DashboardSeasonAdapterItem>>
    val openSettings: MutableLiveData<Event>
}

//endregion

class DashboardViewModel(
    historyDB: HistoryDB
) : BaseViewModel(), DashboardViewModelInputs, DashboardViewModelOutputs {

    private val selectedSeason: ConflatedBroadcastChannel<Int> =
        ConflatedBroadcastChannel(currentYear)

    override val openSettings: MutableLiveData<Event> = MutableLiveData()

    override val years: MutableLiveData<List<DashboardYearItem>> = MutableLiveData()
    override val seasonList: LiveData<List<DashboardSeasonAdapterItem>> = historyDB
        .allHistory()
        .map { historyList ->
            historyList.map { history ->
                val list: MutableList<DashboardSeasonAdapterItem> = mutableListOf()
                list.add(DashboardSeasonAdapterItem.Header(history.season))
                list.addAll(history.rounds
                    .map {
                        DashboardSeasonAdapterItem.Track(
                            season = it.season,
                            round = it.round,
                            date = it.date,
                            circuitId = it.circuitId,
                            trackName = it.raceName,
                            trackNationality = it.country,
                            trackISO = it.countryISO
                        )
                    })
                history.season to list
            }
        }
        .combinePair(selectedSeason.asFlow())
        .map { (list, season) ->
            list.firstOrNull { it.first == season }?.second
        }
        .filterNotNull()
        .asLiveData(viewModelScope.coroutineContext)

    var inputs: DashboardViewModelInputs = this
    var outputs: DashboardViewModelOutputs = this

    init {
        val list = mutableListOf<DashboardYearItem>()
        list.add(DashboardYearItem.Header)
        list.addAll(
            List((currentYear + 1) - minimumSupportedYear) {
                DashboardYearItem.Season(minimumSupportedYear + it)
            }.reversed()
        )
        years.value = list
    }


    //region Inputs

    override fun clickSeason(year: Int) {
        selectedSeason.offer(year)
    }

    override fun clickSettings() {
        openSettings.value = Event()
    }

    //endregion

    //region Outputs

    //endregion
}
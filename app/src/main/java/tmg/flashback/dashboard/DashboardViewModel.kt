package tmg.flashback.dashboard

import androidx.lifecycle.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import tmg.flashback.base.BaseViewModel
import tmg.flashback.currentYear
import tmg.flashback.dashboard.swiping.season.DashboardSeasonAdapterItem
import tmg.flashback.dashboard.year.DashboardYearItem
import tmg.flashback.minimumSupportedYear
import tmg.flashback.repo.db.DataDB
import tmg.flashback.repo.db.HistoryDB
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.models.AppBanner
import tmg.flashback.repo.models.AppLockout
import tmg.flashback.repo.models.History
import tmg.utilities.extensions.combinePair
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

//region Inputs

interface DashboardViewModelInputs {
    fun clickSeason(year: Int)
    fun clickSettings()
}

//endregion

//region Outputs

interface DashboardViewModelOutputs {
    val years: LiveData<List<DashboardYearItem>>
    val seasonList: LiveData<List<DashboardSeasonAdapterItem>>
    val openSettings: MutableLiveData<Event>

    val showAppBanner: LiveData<DataEvent<AppBanner>>
    val showAppLockoutMessage: LiveData<DataEvent<AppLockout>>
    val showReleaseNotes: LiveData<Event>
}

//endregion

class DashboardViewModel(
    historyDB: HistoryDB,
    dataDB: DataDB,
    prefsDB: PrefsDB
) : BaseViewModel(), DashboardViewModelInputs, DashboardViewModelOutputs {

    private val selectedSeason: ConflatedBroadcastChannel<Int> = ConflatedBroadcastChannel(currentYear)
    private val historyFlow: Flow<List<History>> = historyDB.allHistory()

    override val openSettings: MutableLiveData<Event> = MutableLiveData()

    override val showAppLockoutMessage: LiveData<DataEvent<AppLockout>> = dataDB
        .appLockout()
        .filter { it != null && it.show }
        .map { DataEvent(it!!) }
        .asLiveData(viewModelScope.coroutineContext)

    override val showAppBanner: LiveData<DataEvent<AppBanner>> = dataDB
        .appBanner()
        .filter { it != null && it.show }
        .map { DataEvent(it!!) }
        .asLiveData(viewModelScope.coroutineContext)

    override val showReleaseNotes: LiveData<Event> = liveData {
        if (prefsDB.shouldShowReleaseNotes) {
            emit(Event())
        }
    }

    override val years: LiveData<List<DashboardYearItem>> = historyFlow
        .map { list ->
            val itemList = mutableListOf<DashboardYearItem>(DashboardYearItem.Header)
            itemList.addAll(list
                .filter { it.season <= currentYear }
                .map { DashboardYearItem.Season(it.season, it.rounds.size) }
                .sortedByDescending { it.year })
            itemList
        }
        .asLiveData(viewModelScope.coroutineContext)

    override val seasonList: LiveData<List<DashboardSeasonAdapterItem>> = historyFlow
        .map { historyList ->
            historyList.map { history ->
                val list: MutableList<DashboardSeasonAdapterItem> = mutableListOf()
                list.add(DashboardSeasonAdapterItem.Header(history.season))
                list.addAll(history.rounds
                    .filter { it.season <= currentYear }
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
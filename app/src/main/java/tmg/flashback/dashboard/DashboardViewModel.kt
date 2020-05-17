package tmg.flashback.dashboard

import androidx.lifecycle.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import tmg.flashback.base.BaseViewModel
import tmg.flashback.dashboard.season.DashboardSeasonAdapterItem
import tmg.flashback.dashboard.year.DashboardMenuItem
import tmg.flashback.dashboard.year.DashboardYearItem
import tmg.flashback.minimumSupportedYear
import tmg.flashback.repo.db.DataDB
import tmg.flashback.repo.db.HistoryDB
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.models.AppBanner
import tmg.flashback.repo.models.AppLockout
import tmg.flashback.repo.models.History
import tmg.flashback.utils.Selected
import tmg.flashback.utils.bottomsheet.BottomSheetItem
import tmg.utilities.extensions.combinePair
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

//region Inputs

interface DashboardViewModelInputs {
    fun clickSeason(year: Int)
    fun clickSettings()
    fun clickMenu()
    fun clickMenuItem(menuItem: DashboardMenuItem)
}

//endregion

//region Outputs

interface DashboardViewModelOutputs {
    val years: LiveData<List<DashboardYearItem>>
    val seasonList: LiveData<List<DashboardSeasonAdapterItem>>
    val menuList: LiveData<List<Selected<BottomSheetItem>>>

    val openMenu: MutableLiveData<Event>
    val openSettings: MutableLiveData<Event>

    val showAppLockoutMessage: LiveData<DataEvent<AppLockout>>
    val showReleaseNotes: LiveData<Event>
}

//endregion

class DashboardViewModel(
    historyDB: HistoryDB,
    dataDB: DataDB,
    prefsDB: PrefsDB
) : BaseViewModel(), DashboardViewModelInputs, DashboardViewModelOutputs {

    private val selectedSeason: ConflatedBroadcastChannel<Int> = ConflatedBroadcastChannel()
    private val selectedMenuItem: ConflatedBroadcastChannel<DashboardMenuItem> = ConflatedBroadcastChannel(DashboardMenuItem.TRACK_LIST)
    private val historyFlow: Flow<List<History>> = historyDB.allHistory()

    override val openSettings: MutableLiveData<Event> = MutableLiveData()
    override val openMenu: MutableLiveData<Event> = MutableLiveData()

    override val showAppLockoutMessage: LiveData<DataEvent<AppLockout>> = dataDB
        .appLockout()
        .filter { it != null && it.show }
        .map { DataEvent(it!!) }
        .asLiveData(viewModelScope.coroutineContext)

    private val showAppBanner: LiveData<DataEvent<AppBanner>> = dataDB
        .appBanner()
        .filter { it != null && it.show }
        .map { DataEvent(it!!) }
        .asLiveData(viewModelScope.coroutineContext)

    override val showReleaseNotes: LiveData<Event> = liveData {
        if (prefsDB.shouldShowReleaseNotes) {
            emit(Event())
        }
    }

    override val menuList: LiveData<List<Selected<BottomSheetItem>>> = selectedMenuItem
        .asFlow()
        .map { selected ->
            DashboardMenuItem.values().map {
                Selected(
                    BottomSheetItem(it.ordinal, it.icon, it.msg), selected == it
                )
            }
        }
        .asLiveData(viewModelScope.coroutineContext)

    /**
     * Year list that's shown on the initial app load
     */
    override val years: LiveData<List<DashboardYearItem>> = historyFlow
        .combinePair(dataDB.appBanner())
        .map { (list, banner) ->
            val itemList = mutableListOf<DashboardYearItem>(DashboardYearItem.Header)
            if (banner?.show == true && banner.message != null) {
                itemList.add(DashboardYearItem.Banner(banner.message ?: ""))
            }
            itemList.addAll(list
                .map { DashboardYearItem.Season(it.season, it.completed, it.upcoming) }
                .sortedByDescending { it.year })
            itemList
        }
        .asLiveData(viewModelScope.coroutineContext)

    /**
     * List of all the tracks in a given season
     */
    override val seasonList: LiveData<List<DashboardSeasonAdapterItem>> = historyFlow
        .map { historyList ->
            historyList.map { history ->
                val list: MutableList<DashboardSeasonAdapterItem> = mutableListOf()
                list.add(DashboardSeasonAdapterItem.Header(history.season, (history.season - minimumSupportedYear) + 1, history.rounds.size))
                list.addAll(history.rounds
                    .map {
                        DashboardSeasonAdapterItem.Track(
                            season = it.season,
                            round = it.round,
                            date = it.date,
                            raceName = it.raceName,
                            circuitId = it.circuitId,
                            trackName = it.circuitName,
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

    override fun clickMenuItem(menuItem: DashboardMenuItem) {
        selectedMenuItem.offer(menuItem)
    }

    override fun clickMenu() {
        openMenu.value = Event()
    }

    //endregion

    //region Outputs

    //endregion
}
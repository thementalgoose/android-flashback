package tmg.flashback.home.static

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tmg.flashback.base.BaseViewModel
import tmg.flashback.extensions.combinePair
import tmg.flashback.extensions.filterNotEmpty
import tmg.flashback.extensions.filterNotNull
import tmg.flashback.home.trackpicker.TrackModel
import tmg.flashback.repo.db.DataDB
import tmg.flashback.repo.db.HistoryDB
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.models.AppLockout
import tmg.flashback.utils.DataEvent
import tmg.flashback.utils.Event
import tmg.flashback.utils.SeasonRound

//region Inputs

interface HomeStaticViewModelInputs {
    fun select(season: Int, round: Int)
    fun clickTrackList()
    fun closeTrackList()
}

//endregion

//region Outputs

interface HomeStaticViewModelOutputs {
    val showSeasonRound: LiveData<Pair<Int, Int>>
    val openTrackList: LiveData<DataEvent<Pair<Int, Int>>>
    val closeTrackList: LiveData<Event>
    val circuitInfo: LiveData<TrackModel>

    val showError: LiveData<Event>

    val showAppLockoutMessage: LiveData<DataEvent<AppLockout>>
    val showReleaseNotes: LiveData<Event>
}

//endregion

class HomeStaticViewModel(
    historyDB: HistoryDB,
    prefsDB: PrefsDB,
    private val dataDB: DataDB
) : BaseViewModel(), HomeStaticViewModelInputs, HomeStaticViewModelOutputs {

    private val seasonRound: ConflatedBroadcastChannel<SeasonRound> = ConflatedBroadcastChannel()

    private val openTrackEvent: BroadcastChannel<Unit> = BroadcastChannel(BUFFERED)
    private val closedTrackEvent: ConflatedBroadcastChannel<Unit> = ConflatedBroadcastChannel()

    override val openTrackList: LiveData<DataEvent<Pair<Int, Int>>> = openTrackEvent
        .asFlow()
        .map { seasonRound.valueOrNull }
        .filter { it != null }
        .map {
            DataEvent(it!!)
        }
        .asLiveData(viewModelScope.coroutineContext)

    override val closeTrackList: LiveData<Event> = closedTrackEvent
        .asFlow()
        .map { Event() }
        .asLiveData(viewModelScope.coroutineContext)

    override val showAppLockoutMessage: LiveData<DataEvent<AppLockout>> = dataDB
        .appLockout()
        .filter { it != null && it.show }
        .map { DataEvent(it!!) }
        .asLiveData(viewModelScope.coroutineContext)

    override val showReleaseNotes: LiveData<Event> = liveData {
        if (prefsDB.isCurrentAppVersionNew) {
            emit(Event())
        }
    }

    override val showSeasonRound: LiveData<Pair<Int, Int>> = seasonRound
        .asFlow()
        .asLiveData()

    override val circuitInfo: LiveData<TrackModel> = historyDB.allHistory()
        .filterNotEmpty()
        .combine(seasonRound.asFlow()) { list, seasonRound ->
            Log.i("Flashback", "Combine step called $seasonRound - $list")
            list.firstOrNull { it.season == seasonRound.first }
                ?.rounds
                ?.firstOrNull { it.round == seasonRound.second }
        }
        .filterNotNull()
        .map { round ->
            TrackModel(
                season = round.season,
                round = round.round,
                circuitName = round.circuitName,
                country = round.country,
                countryKey = round.countryISO
            )
        }
        .flowOn(Dispatchers.IO)
        .asLiveData(viewModelScope.coroutineContext)

    override val showError: LiveData<Event> = historyDB.allHistory()
        .filter { it.isEmpty() }
        .map { Event() }
        .flowOn(Dispatchers.IO)
        .asLiveData(viewModelScope.coroutineContext)

    var inputs: HomeStaticViewModelInputs = this
    var outputs: HomeStaticViewModelOutputs = this

    init {
        select(prefsDB.selectedYear, 1)
    }

    //region Inputs

    override fun select(season: Int, round: Int) {

        seasonRound.offer(SeasonRound(season, round))
    }

    override fun clickTrackList() {

        openTrackEvent.offer(Unit)
    }

    override fun closeTrackList() {

        closedTrackEvent.offer(Unit)
    }

    //endregion
}
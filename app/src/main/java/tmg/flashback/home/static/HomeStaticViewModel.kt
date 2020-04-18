package tmg.flashback.home.static

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tmg.flashback.base.BaseViewModel
import tmg.flashback.home.trackpicker.TrackModel
import tmg.flashback.repo.db.DataDB
import tmg.flashback.repo.db.HistoryDB
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.db.SeasonOverviewDB
import tmg.flashback.repo.models.AppLockout
import tmg.flashback.repo.models.History
import tmg.flashback.utils.DataEvent
import tmg.flashback.utils.Event
import tmg.flashback.utils.Selected

//region Inputs

interface HomeStaticViewModelInputs {
    fun select(season: Int, round: Int?)
    fun browse(season: Int)
    fun clickTrackList()
    fun closeTrackList()
}

//endregion

//region Outputs

interface HomeStaticViewModelOutputs {
    val showSeasonRound: MutableLiveData<Pair<Int, Int>>
    val openTrackList: MutableLiveData<DataEvent<Pair<Int, Int>>>
    val closeTrackList: MutableLiveData<Event>
    val switchYearList: MutableLiveData<List<Selected<String>>>
    val switchTrackList: MutableLiveData<List<Selected<TrackModel>>>
    val circuitInfo: MutableLiveData<TrackModel>

    val showAppLockoutMessage: MutableLiveData<DataEvent<AppLockout>>

    val showReleaseNotes: MutableLiveData<Event>
}

//endregion

class HomeStaticViewModel(
    private val seasonDB: SeasonOverviewDB,
    private val historyDB: HistoryDB,
    prefsDB: PrefsDB,
    private val dataDB: DataDB
): BaseViewModel(), HomeStaticViewModelInputs, HomeStaticViewModelOutputs {

    private val initialSeason: Int = prefsDB.selectedYear

    private var season: Int = initialSeason
    private var round: Int = 1

    private var browsingSeason: Int = initialSeason

    override val showSeasonRound: MutableLiveData<Pair<Int, Int>> = MutableLiveData()
    override val openTrackList: MutableLiveData<DataEvent<Pair<Int, Int>>> = MutableLiveData()
    override val closeTrackList: MutableLiveData<Event> = MutableLiveData()
    override val switchTrackList: MutableLiveData<List<Selected<TrackModel>>> = MutableLiveData()
    override val switchYearList: MutableLiveData<List<Selected<String>>> = MutableLiveData()
    override val circuitInfo: MutableLiveData<TrackModel> = MutableLiveData()

    override val showAppLockoutMessage: MutableLiveData<DataEvent<AppLockout>> = MutableLiveData()
    override val showReleaseNotes: MutableLiveData<Event> = MutableLiveData()

    private var historyList: List<History> = emptyList()

    var inputs: HomeStaticViewModelInputs = this
    var outputs: HomeStaticViewModelOutputs = this

    init {
        select(prefsDB.selectedYear, 1)

        if (prefsDB.isCurrentAppVersionNew) {
            showReleaseNotes.value = Event()
        }

        viewModelScope.launch(Dispatchers.IO) {
            dataDB.appLockout().collect {
                if (it.show) {
                    showAppLockoutMessage.postValue(DataEvent(it))
                }
            }
        }
    }

    //region Inputs

    override fun select(season: Int, round: Int?) {

        this.season = season
        this.round = round ?: 1
        this.browsingSeason = season

        showSeasonRound.value = Pair(this.season, this.round)

        updateCircuitInfo()
        updateSheet()
    }

    override fun browse(season: Int) {

        this.browsingSeason = season

        processYearList()
        processTrackList()
    }

    override fun clickTrackList() {

        openTrackList.value = DataEvent(Pair(season, round))
    }

    override fun closeTrackList() {

        closeTrackList.value = Event()
    }

    //endregion

    private fun updateCircuitInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val round = seasonDB.getSeasonRound(season, round)
            if (round != null) {
                circuitInfo.postValue(TrackModel(
                    season = round.season,
                    round = round.round,
                    circuitName = round.circuit.name,
                    country = round.circuit.country,
                    countryKey = round.circuit.countryISO
                ))
            }
            else {
                // TODO: Show some kind of placeholder that we cannot get the round
            }
        }
    }

    private fun updateSheet() {
        viewModelScope.launch(Dispatchers.IO) {
            if (historyList.isEmpty()) {
                val history = historyDB.allHistory()
                historyList = history
            }

            processTrackList()
            processYearList()
        }
    }

    private fun processTrackList() {
        switchTrackList.postValue(historyList
            .firstOrNull { it.season == browsingSeason }
            ?.rounds
            ?.map {
                Selected(TrackModel(browsingSeason, it.round, it.raceName, it.country, it.countryISO), isSelected = it.round == round && it.season == season)
            }
            ?.sortedBy { it.value.round } ?: emptyList())
    }

    private fun processYearList() {
        switchYearList.postValue(historyList
            .map { Selected(it.season.toString(), isSelected = it.season == browsingSeason) }
            .sortedByDescending { it.value })
    }
}
package tmg.f1stats.home.static

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tmg.f1stats.base.BaseViewModel
import tmg.f1stats.home.trackpicker.TrackModel
import tmg.f1stats.repo.db.HistoryDB
import tmg.f1stats.repo.db.PrefsDB
import tmg.f1stats.repo.db.SeasonOverviewDB
import tmg.f1stats.repo.models.History
import tmg.f1stats.utils.DataEvent
import tmg.f1stats.utils.Event
import tmg.f1stats.utils.Selected

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
    val openTrackList: MutableLiveData<DataEvent<Boolean>>
    val switchYearList: MutableLiveData<List<Selected<String>>>
    val switchTrackList: MutableLiveData<List<Selected<TrackModel>>>
    val circuitInfo: MutableLiveData<TrackModel>
}

//endregion

class HomeStaticViewModel(
    private val seasonDB: SeasonOverviewDB,
    private val historyDB: HistoryDB,
    prefsDB: PrefsDB
): BaseViewModel(), HomeStaticViewModelInputs, HomeStaticViewModelOutputs {

    private val initialSeason: Int = prefsDB.selectedYear

    private var season: Int = initialSeason
    private var round: Int = 1

    private var browsingSeason: Int = initialSeason

    override val showSeasonRound: MutableLiveData<Pair<Int, Int>> = MutableLiveData()
    override val openTrackList: MutableLiveData<DataEvent<Boolean>> = MutableLiveData()
    override val switchTrackList: MutableLiveData<List<Selected<TrackModel>>> = MutableLiveData()
    override val switchYearList: MutableLiveData<List<Selected<String>>> = MutableLiveData()
    override val circuitInfo: MutableLiveData<TrackModel> = MutableLiveData()

    private var historyList: List<History> = emptyList()

    var inputs: HomeStaticViewModelInputs = this
    var outputs: HomeStaticViewModelOutputs = this

    init {
        select(prefsDB.selectedYear, 1)
    }

    //region Inputs

    override fun select(season: Int, round: Int?) {

        this.season = season
        this.round = round ?: 1
        this.browsingSeason = season

        showSeasonRound.value = Pair(this.season, this.round)

        updateSheet()
        updateCircuitInfo()
    }

    override fun browse(season: Int) {

        this.browsingSeason = season

        processYearList()
        processTrackList()
    }

    override fun clickTrackList() {

        openTrackList.value = DataEvent(true)
    }

    override fun closeTrackList() {

        openTrackList.value = DataEvent(false)
    }

    //endregion

    private fun updateCircuitInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            seasonDB.getSeasonRound(season, round)?.let {
                circuitInfo.postValue(TrackModel(
                    season = it.season,
                    round = it.round,
                    circuitName = it.circuit.name,
                    country = it.circuit.country,
                    countryKey = it.circuit.countryISO
                ))
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
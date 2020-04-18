package tmg.flashback.home.trackpicker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tmg.flashback.base.BaseViewModel
import tmg.flashback.repo.db.SeasonOverviewDB
import tmg.flashback.supportedYears
import tmg.flashback.utils.DataEvent
import tmg.flashback.utils.SeasonRound
import tmg.flashback.utils.Selected

//region Inputs

interface TrackPickerViewModelInputs {
    fun initialSeasonRound(season: Int, round: Int)
    fun clickSeason(season: Int)
    fun clickRound(round: Int)
}

//endregion

//region Outputs

interface TrackPickerViewModelOutputs {

    val selected: MutableLiveData<DataEvent<SeasonRound>>
    val yearList: MutableLiveData<List<Selected<String>>>
    val trackList: MutableLiveData<List<Selected<TrackModel>>>
}

//endregion

class TrackPickerViewModel(
    private val seasonDB: SeasonOverviewDB
): BaseViewModel(), TrackPickerViewModelInputs, TrackPickerViewModelOutputs {

    private var season: Int = -1
    private var round: Int = -1

    override val yearList: MutableLiveData<List<Selected<String>>> = MutableLiveData()
    override val trackList: MutableLiveData<List<Selected<TrackModel>>> = MutableLiveData()
    override val selected: MutableLiveData<DataEvent<SeasonRound>> = MutableLiveData()

    var inputs: TrackPickerViewModelInputs = this
    var outputs: TrackPickerViewModelOutputs = this

    //region Inputs

    override fun initialSeasonRound(season: Int, round: Int) {
        this.season = season
        this.round = round

        clickSeason(season)
    }

    override fun clickSeason(season: Int) {
        this.season = season

        processList()
    }

    override fun clickRound(round: Int) {

        selected.value = DataEvent(SeasonRound(season, round))
    }

    //endregion

    private fun processList() {
        yearList.postValue(supportedYears
            .map { Selected(it.toString(), it == season)}
            .sortedByDescending { it.value }
        )

        viewModelScope.launch(Dispatchers.IO) {
            val seasonOverview = seasonDB.getSeasonOverview(season)
            trackList.postValue(seasonOverview
                .map { race -> Selected(TrackModel(race.season, race.round, race.circuit.name, race.circuit.country, race.circuit.countryISO)) }
                .sortedBy { it.value.round }
                .map { Selected(it.value, isSelected = round == it.value.round && season == it.value.season) })
        }
    }

}
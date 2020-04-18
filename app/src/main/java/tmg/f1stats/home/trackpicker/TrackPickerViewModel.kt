package tmg.f1stats.home.trackpicker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tmg.f1stats.base.BaseViewModel
import tmg.f1stats.repo.db.SeasonOverviewDB
import tmg.f1stats.supportedYears
import tmg.f1stats.utils.Selected

//region Inputs

interface TrackPickerViewModelInputs {
    fun initialSeasonRound(season: Int, round: Int)
    fun clickSeason(season: Int)
}

//endregion

//region Outputs

interface TrackPickerViewModelOutputs {

    val yearList: MutableLiveData<List<Selected<String>>>
    val trackList: MutableLiveData<List<Selected<TrackModel>>>
}

//endregion

class TrackPickerViewModel(
    private val seasonDB: SeasonOverviewDB
): BaseViewModel(), TrackPickerViewModelInputs, TrackPickerViewModelOutputs {

    private var season: Int = -1
    private var round: Int = -1

    private var selectedSeason: Int = season

    override val yearList: MutableLiveData<List<Selected<String>>> = MutableLiveData()
    override val trackList: MutableLiveData<List<Selected<TrackModel>>> = MutableLiveData()

    var inputs: TrackPickerViewModelInputs = this
    var outputs: TrackPickerViewModelOutputs = this

    //region Inputs

    override fun initialSeasonRound(season: Int, round: Int) {
        this.season = season
        this.round = round

        clickSeason(season)
    }

    override fun clickSeason(season: Int) {
        selectedSeason = season

        processList()
    }

    private fun processList() {
        yearList.postValue(supportedYears.map { Selected(it.toString(), it == selectedSeason)})

        viewModelScope.launch(Dispatchers.IO) {
            val seasonOverview = seasonDB.getSeasonOverview(season)
            trackList.postValue(seasonOverview
                .map { race -> Selected(TrackModel(race.season, race.round, race.circuit.name, race.circuit.country, race.circuit.countryISO)) }
                .map { Selected(it.value, isSelected = round == it.value.round && season == it.value.season) })
        }
    }

    //endregion
}
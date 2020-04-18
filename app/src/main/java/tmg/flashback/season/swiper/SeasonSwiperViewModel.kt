package tmg.flashback.season.swiper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tmg.flashback.base.BaseViewModel
import tmg.flashback.repo.db.SeasonOverviewDB

//region Inputs

interface SeasonViewModelInputs {
    fun initialise(season: Int)
}

//endregion

//region Outputs

interface SeasonViewModelOutputs {
    val seasonRounds: MutableLiveData<List<SeasonSwiperAdapterModel>>
}

//endregion

class SeasonViewModel(
    private val seasonOverviewDB: SeasonOverviewDB
) : BaseViewModel(), SeasonViewModelInputs,
    SeasonViewModelOutputs {

    private var season: Int = -1

    override val seasonRounds: MutableLiveData<List<SeasonSwiperAdapterModel>> = MutableLiveData()

    var inputs: SeasonViewModelInputs = this
    var outputs: SeasonViewModelOutputs = this

    init {

    }

    //region Inputs

    override fun initialise(season: Int) {
        this.season = season

        viewModelScope.launch(Dispatchers.IO) {
            val result = seasonOverviewDB.getSeasonOverview(season)
            seasonRounds.postValue(result
                .map {
                    SeasonSwiperAdapterModel(
                        it.season,
                        it.round,
                        it.circuit.countryISO,
                        it.name,
                        it.circuit.name
                    )
                }
                .sortedBy { it.round })
        }
    }

    //endregion
}
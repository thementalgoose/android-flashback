package tmg.f1stats.season.swiper

import androidx.lifecycle.MutableLiveData
import tmg.f1stats.base.BaseViewModel
import tmg.f1stats.repo.db.SeasonOverviewDB

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

        async {
            val result = seasonOverviewDB.getSeasonOverview(season)
            seasonRounds.value = result
                .map {
                    SeasonSwiperAdapterModel(
                        it.season,
                        it.round,
                        it.circuit.countryISO,
                        it.name,
                        it.circuit.name
                    )
                }
                .sortedBy { it.round }
        }
    }

    //endregion
}
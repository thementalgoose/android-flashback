package tmg.f1stats.home.season

import io.reactivex.rxjava3.subjects.BehaviorSubject
import tmg.f1stats.base.BaseViewModel

//region Inputs

interface SeasonRaceViewModelInputs {
    fun initialise(season: Int, round: Int)
}

//endregion

//region Outputs

interface SeasonRaceViewModelOutputs {

}

//endregion

class SeasonRaceViewModel: BaseViewModel(), SeasonRaceViewModelInputs, SeasonRaceViewModelOutputs {

    private var seasonRound: BehaviorSubject<Pair<Int, Int>> = BehaviorSubject.create()

    var inputs: SeasonRaceViewModelInputs = this
    var outputs: SeasonRaceViewModelOutputs = this

    init {

    }

    //region Inputs

    override fun initialise(season: Int, round: Int) {
        seasonRound.onNext(Pair(season, round))
    }

    //endregion

    //region Outputs

    //endregion
}
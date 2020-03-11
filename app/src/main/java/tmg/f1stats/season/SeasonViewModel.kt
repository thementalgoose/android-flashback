package tmg.f1stats.season

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import tmg.f1stats.base.BaseViewModel
import tmg.f1stats.repo.db.SeasonOverviewDB

//region Inputs

interface SeasonViewModelInputs {
    fun initialise(season: Int)
}

//endregion

//region Outputs

interface SeasonViewModelOutputs {
    fun seasonRounds(): Observable<List<SeasonAdapterModel>>
}

//endregion

class SeasonViewModel(
    val seasonOverviewDB: SeasonOverviewDB
): BaseViewModel(), SeasonViewModelInputs, SeasonViewModelOutputs {

    private var seasonEvent: BehaviorSubject<Int> = BehaviorSubject.create()

    var inputs: SeasonViewModelInputs = this
    var outputs: SeasonViewModelOutputs = this
    
    init { 
        
    }
    
    //region Inputs

    override fun initialise(season: Int) {
        seasonEvent.onNext(season)
    }
    
    //endregion
    
    //region Outputs

    override fun seasonRounds(): Observable<List<SeasonAdapterModel>> {
        return seasonEvent
            .switchMap { seasonOverviewDB.getSeasonOverview(it) }
            .map { rounds ->
                rounds.map { SeasonAdapterModel(it.season, it.round, it.raceKey, it.raceName, it.circuit.circuitName) }
            }
    }
    
    //endregion
}
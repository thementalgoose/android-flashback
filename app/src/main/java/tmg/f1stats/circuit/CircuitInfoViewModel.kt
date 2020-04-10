package tmg.f1stats.circuit

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import tmg.f1stats.base.BaseViewModel
import tmg.f1stats.repo.db.CircuitDB
import tmg.f1stats.repo.models.Circuit

//region Inputs

interface CircuitInfoViewModelInputs {
    fun circuitId(circuitId: String)
}

//endregion

//region Outputs

interface CircuitInfoViewModelOutputs {
    fun circuitInfo(): Observable<Circuit>
}

//endregion

class CircuitInfoViewModel(
    private val circuitDB: CircuitDB
): BaseViewModel(), CircuitInfoViewModelInputs, CircuitInfoViewModelOutputs {

    private val circuitIdEvent: BehaviorSubject<String> = BehaviorSubject.create()

    private val circuitInfoObservable: Observable<Circuit> = circuitIdEvent
        .switchMap { circuitDB.getCircuit(it) }

    var inputs: CircuitInfoViewModelInputs = this
    var outputs: CircuitInfoViewModelOutputs = this

    init {

    }

    //region Inputs

    override fun circuitId(circuitId: String) {
        circuitIdEvent.onNext(circuitId)
    }

    //endregion

    //region Outputs

    override fun circuitInfo(): Observable<Circuit> {
        return circuitInfoObservable
    }

    //endregion
}
package tmg.f1stats.repo.db

import io.reactivex.rxjava3.core.Observable
import tmg.f1stats.repo.models.Circuit

interface CircuitDB {
    fun getCircuit(circuitId: String): Observable<Circuit>
}
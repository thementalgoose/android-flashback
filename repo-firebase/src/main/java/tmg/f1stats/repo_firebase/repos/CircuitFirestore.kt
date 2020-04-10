package tmg.f1stats.repo_firebase.repos

import io.reactivex.rxjava3.core.Observable
import tmg.f1stats.repo.db.CircuitDB
import tmg.f1stats.repo.models.Circuit

class CircuitFirestore: CircuitDB {

    override fun getCircuit(circuitId: String): Observable<Circuit> {
        return Observable.just(Circuit(
            id = circuitId,
            name = "Albert Park Circuit",
            wikiUrl = "https://en.wikipedia.org/wiki/Albert_Park_Circuit",
            locality = "Australian",
            country = "Australia",
            countryISO = "AUS",
            locationLat = -37.849722,
            locationLng = 144.968333
        ))
    }
}
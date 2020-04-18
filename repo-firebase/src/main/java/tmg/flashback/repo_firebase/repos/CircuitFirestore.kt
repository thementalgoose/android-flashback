package tmg.flashback.repo_firebase.repos

import tmg.flashback.repo.db.CircuitDB
import tmg.flashback.repo.models.Circuit

class CircuitFirestore: CircuitDB {

    override suspend fun getCircuit(circuitId: String): Circuit? {
        return Circuit(
            id = circuitId,
            name = "Albert Park Circuit",
            wikiUrl = "https://en.wikipedia.org/wiki/Albert_Park_Circuit",
            locality = "Australian",
            country = "Australia",
            countryISO = "AUS",
            locationLat = -37.849722,
            locationLng = 144.968333
        )
    }
}
package tmg.flashback.firebase.repos

import kotlinx.coroutines.flow.Flow
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.converters.convert
import tmg.flashback.firebase.crash.FirebaseCrashManager
import tmg.flashback.firebase.models.FCircuit
import tmg.flashback.data.db.stats.CircuitRepository
import tmg.flashback.data.models.stats.Circuit

class CircuitFirestore(
    crashManager: FirebaseCrashManager
) : FirebaseRepo(crashManager), CircuitRepository {
    override fun getCircuit(id: String): Flow<Circuit?> {
        crashManager.logInfo("document(circuits/$id)")
        return document("circuits/$id")
            .getDoc<FCircuit, Circuit> { it.convert() }
    }
}

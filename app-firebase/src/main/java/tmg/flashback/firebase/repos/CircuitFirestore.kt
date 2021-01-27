package tmg.flashback.firebase.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.converters.convert
import tmg.flashback.firebase.crash.FirebaseCrashManager
import tmg.flashback.firebase.models.FCircuit
import tmg.flashback.repo.db.stats.CircuitRepository
import tmg.flashback.repo.models.stats.Circuit

class CircuitFirestore(
    crashManager: FirebaseCrashManager
) : FirebaseRepo(crashManager), CircuitRepository {
    override fun getCircuit(id: String): Flow<Circuit?> {
        crashManager.logInfo("document(circuits/$id)")
        return document("circuits/$id")
            .getDoc<FCircuit, Circuit> { it.convert() }
    }
}

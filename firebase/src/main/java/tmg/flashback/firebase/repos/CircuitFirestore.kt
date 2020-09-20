package tmg.flashback.firebase.repos

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.converters.convert
import tmg.flashback.firebase.models.FCircuit
import tmg.flashback.repo.db.CrashReporter
import tmg.flashback.repo.db.stats.CircuitDB
import tmg.flashback.repo.models.stats.Circuit

@ExperimentalCoroutinesApi
class CircuitFirestore(
    crashReporter: CrashReporter
) : FirebaseRepo(crashReporter), CircuitDB {
    override fun getCircuit(id: String): Flow<Circuit?> {
        return document("circuits/$id")
            .getDoc<FCircuit>()
            .map {
                it?.convert()
            }
    }
}

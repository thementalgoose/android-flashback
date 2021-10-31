package tmg.flashback.firebase.repos

import kotlinx.coroutines.flow.Flow
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.models.FCircuit
import tmg.flashback.data.db.stats.CircuitRepository
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.firebase.mappers.CircuitMapper

class CircuitFirestore(
        crashController: CrashController,
        private val circuitMapper: CircuitMapper
) : FirebaseRepo(crashController), CircuitRepository {

    override fun getCircuit(id: String): Flow<tmg.flashback.formula1.model.Circuit?> {
        crashController.log("document(circuits/$id)")
        return document("circuits/$id")
            .getDoc<FCircuit, tmg.flashback.formula1.model.Circuit> {
                circuitMapper.mapCircuit(it)
        }
    }
}

package tmg.flashback.firebase.repos

import kotlinx.coroutines.flow.Flow
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.converters.convert
import tmg.flashback.firebase.models.FConstructorOverview
import tmg.flashback.data.db.stats.ConstructorRepository
import tmg.flashback.data.models.stats.ConstructorOverview

class ConstructorFirestore(
    crashController: CrashController
) : FirebaseRepo(crashController), ConstructorRepository {

    override fun getConstructorOverview(constructorId: String): Flow<ConstructorOverview?> {
        crashController.log("document(constructors/$constructorId)")
        return document("constructors/$constructorId")
                .getDoc<FConstructorOverview, ConstructorOverview> { it.convert() }
    }
}
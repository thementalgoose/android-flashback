package tmg.flashback.firebase.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.converters.convert
import tmg.flashback.firebase.crash.FirebaseCrashManager
import tmg.flashback.firebase.models.FConstructorOverview
import tmg.flashback.data.db.stats.ConstructorRepository
import tmg.flashback.data.models.stats.ConstructorOverview

class ConstructorFirestore(
        crashManager: FirebaseCrashManager
) : FirebaseRepo(crashManager), ConstructorRepository {

    override fun getConstructorOverview(constructorId: String): Flow<ConstructorOverview?> {
        crashManager.logInfo("document(constructors/$constructorId)")
        return document("constructors/$constructorId")
                .getDoc<FConstructorOverview, ConstructorOverview> { it.convert() }
    }
}
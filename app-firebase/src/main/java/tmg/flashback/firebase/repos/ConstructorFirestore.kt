package tmg.flashback.firebase.repos

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.converters.convert
import tmg.flashback.firebase.models.FConstructorOverview
import tmg.flashback.repo.db.CrashManager
import tmg.flashback.repo.db.stats.ConstructorRepository
import tmg.flashback.repo.models.stats.ConstructorOverview

class ConstructorFirestore(
        crashManager: CrashManager
) : FirebaseRepo(crashManager), ConstructorRepository {

    override fun getConstructorOverview(constructorId: String): Flow<ConstructorOverview?> {
        return document("constructors/$constructorId")
                .getDoc<FConstructorOverview>()
                .map {
                    it?.convert()
                }
    }
}
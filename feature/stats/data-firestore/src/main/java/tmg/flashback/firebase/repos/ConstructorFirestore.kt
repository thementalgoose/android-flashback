package tmg.flashback.firebase.repos

import kotlinx.coroutines.flow.Flow
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.models.FConstructorOverview
import tmg.flashback.data.db.stats.ConstructorRepository
import tmg.flashback.formula1.model.ConstructorOverview
import tmg.flashback.firebase.mappers.ConstructorMapper

class ConstructorFirestore(
        crashController: CrashController,
        private val constructorMapper: ConstructorMapper
) : FirebaseRepo(crashController), ConstructorRepository {

    override fun getConstructorOverview(constructorId: String): Flow<tmg.flashback.formula1.model.ConstructorOverview?> {
        crashController.log("document(constructors/$constructorId)")
        return document("constructors/$constructorId")
                .getDoc<FConstructorOverview, tmg.flashback.formula1.model.ConstructorOverview> {
                    constructorMapper.mapConstructorOverview(it)
                }
    }
}
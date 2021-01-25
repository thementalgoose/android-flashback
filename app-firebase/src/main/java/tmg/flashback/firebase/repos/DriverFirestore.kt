package tmg.flashback.firebase.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.flashback.firebase.crash.FirebaseCrashManagerImpl
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.converters.convert
import tmg.flashback.firebase.crash.FirebaseCrashManager
import tmg.flashback.firebase.models.FDriverOverview
import tmg.flashback.repo.db.stats.DriverRepository
import tmg.flashback.repo.models.stats.DriverOverview

class DriverFirestore(
    crashManager: FirebaseCrashManager
) : FirebaseRepo(crashManager), DriverRepository {

    override fun getDriverOverview(driverId: String): Flow<DriverOverview?> {
        crashManager.log("DriverFirestore.getDriverOverview($driverId)")
        return document("drivers/$driverId")
                .getDoc<FDriverOverview>()
                .map {
                    it?.convert()
                }
    }
}
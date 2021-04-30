package tmg.flashback.firebase.repos

import kotlinx.coroutines.flow.Flow
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.converters.convert
import tmg.flashback.firebase.models.FDriverOverview
import tmg.flashback.data.db.stats.DriverRepository
import tmg.flashback.data.models.stats.DriverOverview
import tmg.flashback.firebase.FirestoreCrashManager

class DriverFirestore(
    crashManager: FirestoreCrashManager
) : FirebaseRepo(crashManager), DriverRepository {

    override fun getDriverOverview(driverId: String): Flow<DriverOverview?> {
        crashManager.logInfo("document(drivers/$driverId)")
        return document("drivers/$driverId")
                .getDoc<FDriverOverview, DriverOverview> { it.convert() }
    }
}
package tmg.flashback.firebase.repos

import kotlinx.coroutines.flow.Flow
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.models.FDriverOverview
import tmg.flashback.data.db.stats.DriverRepository
import tmg.flashback.data.models.stats.DriverOverview
import tmg.flashback.firebase.mappers.DriverMapper

class DriverFirestore(
        crashController: CrashController,
        private val driverMapper: DriverMapper
) : FirebaseRepo(crashController), DriverRepository {

    override fun getDriverOverview(driverId: String): Flow<DriverOverview?> {
        crashController.log("document(drivers/$driverId)")
        return document("drivers/$driverId")
                .getDoc<FDriverOverview, DriverOverview> { driverMapper.mapDriverOverview(it) }
    }
}
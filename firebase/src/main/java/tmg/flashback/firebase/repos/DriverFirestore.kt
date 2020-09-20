package tmg.flashback.firebase.repos

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.converters.convert
import tmg.flashback.firebase.models.FDriverOverview
import tmg.flashback.repo.db.CrashReporter
import tmg.flashback.repo.db.stats.DriverDB
import tmg.flashback.repo.models.stats.DriverOverview

@ExperimentalCoroutinesApi
class DriverFirestore(
        crashReporter: CrashReporter
) : FirebaseRepo(crashReporter), DriverDB {

    override fun getDriverOverview(driverId: String): Flow<DriverOverview?> {
        return document("drivers/$driverId")
                .getDoc<FDriverOverview>()
                .map {
                    it?.convert()
                }
    }
}
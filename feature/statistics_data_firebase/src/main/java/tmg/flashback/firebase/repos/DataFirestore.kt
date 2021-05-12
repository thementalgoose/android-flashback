package tmg.flashback.firebase.repos

import kotlinx.coroutines.flow.Flow
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.data.db.DataRepository
import tmg.flashback.data.models.AppLockout
import tmg.flashback.firebase.converters.convert
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.models.FAppLockout

class DataFirestore(
        crashController: CrashController
): FirebaseRepo(crashController), DataRepository {

    override fun appLockout(): Flow<AppLockout?> {
        crashController.log("document(data/app-lockout)")
        return document("data/app-lockout")
            .getDoc<FAppLockout, AppLockout> { it.convert() }
    }
}
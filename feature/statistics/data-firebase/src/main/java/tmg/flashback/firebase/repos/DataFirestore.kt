package tmg.flashback.firebase.repos

import kotlinx.coroutines.flow.Flow
import tmg.flashback.data.db.DataRepository
import tmg.flashback.data.models.AppLockout
import tmg.flashback.firebase.converters.convert
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.FirestoreCrashManager
import tmg.flashback.firebase.models.FAppLockout

class DataFirestore(
    crashManager: FirestoreCrashManager
): FirebaseRepo(crashManager), DataRepository {

    override fun appLockout(): Flow<AppLockout?> {
        crashManager.logInfo("document(data/app-lockout)")
        return document("data/app-lockout")
            .getDoc<FAppLockout, AppLockout> { it.convert() }
    }
}
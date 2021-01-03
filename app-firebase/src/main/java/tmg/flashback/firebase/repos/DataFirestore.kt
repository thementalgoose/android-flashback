package tmg.flashback.firebase.repos

import kotlinx.coroutines.flow.Flow
import tmg.flashback.firebase.crash.FirebaseCrashManagerImpl
import tmg.flashback.repo.db.DataRepository
import tmg.flashback.repo.models.remoteconfig.AppLockout
import tmg.flashback.firebase.converters.convert
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.crash.FirebaseCrashManager
import tmg.flashback.firebase.models.FAppLockout

class DataFirestore(
    crashManager: FirebaseCrashManager
): FirebaseRepo(crashManager), DataRepository {

    override fun appLockout(): Flow<AppLockout?> {
        return document("data/app-lockout")
            .getDoc<FAppLockout>()
            .convertModel { it?.convert() }
    }
}
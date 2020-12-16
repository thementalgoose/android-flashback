package tmg.flashback.firebase.repos

import kotlinx.coroutines.flow.Flow
import tmg.flashback.repo.db.CrashManager
import tmg.flashback.repo.db.DataRepository
import tmg.flashback.repo.models.AppLockout
import tmg.flashback.firebase.converters.convert
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.models.FAppBanner
import tmg.flashback.firebase.models.FAppLockout

class DataFirestore(
    crashManager: CrashManager
): FirebaseRepo(crashManager), DataRepository {

    override fun appLockout(): Flow<AppLockout?> {
        return document("data/app-lockout")
            .getDoc<FAppLockout>()
            .convertModel { it?.convert() }
    }
}
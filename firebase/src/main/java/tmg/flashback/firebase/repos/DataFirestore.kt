package tmg.flashback.firebase.repos

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import tmg.flashback.repo.db.CrashReporter
import tmg.flashback.repo.db.stats.DataDB
import tmg.flashback.repo.models.AppBanner
import tmg.flashback.repo.models.AppLockout
import tmg.flashback.firebase.converters.convert
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.models.FAppBanner
import tmg.flashback.firebase.models.FAppLockout

@ExperimentalCoroutinesApi
class DataFirestore(
    crashReporter: CrashReporter
): FirebaseRepo(crashReporter), DataDB {

    override fun appLockout(): Flow<AppLockout?> {
        return document("data/app-lockout")
            .getDoc<FAppLockout>()
            .convertModel { it?.convert() }
    }

    override fun appBanner(): Flow<AppBanner?> {
        return document("data/app-banner")
            .getDoc<FAppBanner>()
            .convertModel { it?.convert() }
    }
}
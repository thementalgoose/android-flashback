package tmg.flashback.repo_firebase.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import tmg.flashback.repo.db.CrashReporter
import tmg.flashback.repo.db.DataDB
import tmg.flashback.repo.models.AppLockout
import tmg.flashback.repo_firebase.converters.convert
import tmg.flashback.repo_firebase.firebase.FirebaseRepo
import tmg.flashback.repo_firebase.firebase.getDocument
import tmg.flashback.repo_firebase.models.FAppLockout

class DataFirestore(
    crashReporter: CrashReporter
): FirebaseRepo(crashReporter), DataDB {

    override fun appLockout(): Flow<AppLockout?> {
        return document("data/app-lockout")
            .getDoc<FAppLockout>()
            .convertModel { it?.convert() }
    }
}
package tmg.flashback.repo_firebase.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tmg.flashback.repo.db.DataDB
import tmg.flashback.repo.models.AppLockout

class DataFirestore: DataDB {
    override fun appLockout(): Flow<AppLockout> {
        // TODO: App Lockout
        return flow {
            emit(AppLockout(true, "Test message",  "Help me with google play", "https://www.google.com"))
        }
    }
}
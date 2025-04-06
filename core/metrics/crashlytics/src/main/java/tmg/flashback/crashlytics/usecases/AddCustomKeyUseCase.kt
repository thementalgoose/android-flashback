package tmg.flashback.crashlytics.usecases

import tmg.flashback.crashlytics.model.FirebaseKey
import tmg.flashback.crashlytics.services.FirebaseCrashService
import javax.inject.Inject

class AddCustomKeyUseCase @Inject constructor(
    private val firebaseCrashService: FirebaseCrashService
) {
    operator fun invoke(key: FirebaseKey, value: String) {
        firebaseCrashService.setCustomKey(key, value)
    }
    operator fun invoke(key: FirebaseKey, value: Boolean) {
        firebaseCrashService.setCustomKey(key, value)
    }
}
package tmg.flashback.crashlytics.usecases

import tmg.flashback.crashlytics.model.FirebaseKey
import tmg.flashback.crashlytics.services.FirebaseCrashService
import tmg.flashback.device.repository.PrivacyRepository
import javax.inject.Inject

class InitialiseCrashReportingUseCase @Inject constructor(
    private val privacyRepository: PrivacyRepository,
    private val firebaseCrashService: FirebaseCrashService
) {
    fun initialise(
        deviceUuid: String,
        extraKeys: Map<FirebaseKey, String>,
    ) {
        firebaseCrashService.initialise(
            enableCrashReporting = privacyRepository.crashReporting,
            deviceUuid = deviceUuid,
            extraKeys = extraKeys,
        )
    }
}
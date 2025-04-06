package tmg.flashback.crashlytics.usecases

import android.util.Log
import tmg.flashback.crashlytics.model.FirebaseKey
import tmg.flashback.crashlytics.services.FirebaseCrashService
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.device.repository.PrivacyRepository
import javax.inject.Inject

class InitialiseCrashReportingUseCase @Inject constructor(
    private val privacyRepository: PrivacyRepository,
    private val firebaseCrashService: FirebaseCrashService,
    private val buildConfigManager: BuildConfigManager
) {
    fun initialise(
        deviceUuid: String,
        extraKeys: Map<FirebaseKey, String>,
    ) {
        firebaseCrashService.setCrashlyticsCollectionEnabled(privacyRepository.crashReporting)
        when (privacyRepository.crashReporting) {
            true -> Log.i("Crashlytics", "Enabling crashlytics")
            false -> Log.i("Crashlytics", "Disabling crashlytics")
        }

        firebaseCrashService.setCustomKey(FirebaseKey.Emulator, buildConfigManager.isEmulator)
        firebaseCrashService.setCustomKey(FirebaseKey.Debug, buildConfigManager.isDebug)

        firebaseCrashService.setUserId(deviceUuid)
        firebaseCrashService.setCustomKey(FirebaseKey.DeviceUuid, deviceUuid)
        firebaseCrashService.setCustomKey(FirebaseKey.Brand, buildConfigManager.brand)
        firebaseCrashService.setCustomKey(FirebaseKey.Hardware, buildConfigManager.hardware)
        firebaseCrashService.setCustomKey(FirebaseKey.Board, buildConfigManager.board)
        firebaseCrashService.setCustomKey(FirebaseKey.Fingerprint, buildConfigManager.fingerprint)
        firebaseCrashService.setCustomKey(FirebaseKey.Model, buildConfigManager.model)
        firebaseCrashService.setCustomKey(FirebaseKey.Manufacturer, buildConfigManager.manufacturer)
        firebaseCrashService.setCustomKey(FirebaseKey.Product, buildConfigManager.product)
        firebaseCrashService.setCustomKey(FirebaseKey.Device, buildConfigManager.device)

        extraKeys.forEach { (key, value) ->
            firebaseCrashService.setCustomKey(key, value)
        }
    }
}
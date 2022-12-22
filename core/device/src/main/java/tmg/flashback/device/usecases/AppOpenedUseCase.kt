package tmg.flashback.device.usecases

import android.util.Log
import org.threeten.bp.LocalDate
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.device.managers.FirebaseInstallationManager
import tmg.flashback.device.repository.DeviceRepository
import javax.inject.Inject

class AppOpenedUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val installationManager: FirebaseInstallationManager,
    private val buildConfigManager: BuildConfigManager
) {
    fun run() {
        if (deviceRepository.appOpenedCount == 0) {
            deviceRepository.appFirstOpened = LocalDate.now()
        }
        deviceRepository.appOpenedCount += 1
        deviceRepository.lastAppVersion = buildConfigManager.versionCode
    }

    suspend fun preload() {
        val id = installationManager.getInstallationId()
        if (id != null) {
            Log.i("Flashback", "Saving installation ID as $id")
            deviceRepository.installationId = id
        }
    }
}
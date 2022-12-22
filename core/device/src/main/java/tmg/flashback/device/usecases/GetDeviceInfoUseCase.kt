package tmg.flashback.device.usecases

import android.os.Build
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.device.managers.DeviceConfigManager
import tmg.flashback.device.repository.DeviceRepository
import tmg.utilities.extensions.format
import tmg.utilities.models.DeviceStatus
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDeviceInfoUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val buildConfigManager: BuildConfigManager,
    private val deviceConfigManager: DeviceConfigManager
) {

    fun run(): String {
        val deviceStatus = deviceConfigManager.getDeviceStatus()
        val params = mapOf(
            "Manufacturer" to (deviceStatus?.deviceManufacturer ?: Build.MANUFACTURER),
            "Model" to (deviceStatus?.deviceModel ?: Build.MODEL),
            "OS Version" to (deviceStatus?.osVersion ?: Build.VERSION.RELEASE),
            "SDK Version" to (deviceStatus?.sdkVersion?.toString() ?: Build.VERSION.SDK_INT.toString()),
            "Language" to (deviceStatus?.language ?: Locale.getDefault().language),
            "Screen density" to deviceStatus?.screenDensity?.toString(),
            "Screen resolution" to deviceStatus?.let { "${it.screenWidth}x${it.screenHeight}" },
            "Version name" to "${buildConfigManager.versionName} (${buildConfigManager.versionCode})",
            "Device ID" to deviceRepository.deviceUdid,
            "Installation ID" to deviceRepository.installationId,
            "Installed on" to deviceRepository.appFirstOpened.format("dd MMM yyyy")
        )
        val label = params
            .filter { (_, value) -> value != null }
            .map { (key, value) -> "$key: $value" }
            .joinToString(separator = "\n")
        return "-------- DEVICE INFORMATION --------\n$label"
    }
}
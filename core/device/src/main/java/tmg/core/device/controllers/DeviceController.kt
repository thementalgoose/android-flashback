package tmg.core.device.controllers

import org.threeten.bp.LocalDate
import tmg.core.device.managers.BuildConfigManager
import tmg.core.device.repository.DeviceRepository

/**
 * Controller to handle device prefs and actions that
 *  may require perminent storage
 */
class DeviceController(
    private val deviceRepository: DeviceRepository,
    private val buildConfigManager: BuildConfigManager
) {
    /**
     * To be ran when the app is first opened
     */
    fun appOpened() {
        appOpenedCount += 1
        updateAppVersion()
    }

    var appOpenedCount: Int
        get() = deviceRepository.appOpenedCount
        private set(value) {
            deviceRepository.appOpenedCount = value
        }

    val appFirstBoot: LocalDate
        get() = deviceRepository.appFirstOpened

    //region Device UDID

    val deviceUdid: String
        get() = deviceRepository.deviceUdid

    //endregion

    //region Last app version

    var lastAppVersion: Int
        get() = deviceRepository.lastAppVersion
        private set(value) {
            deviceRepository.lastAppVersion = value
        }

    private fun updateAppVersion() {
        lastAppVersion = buildConfigManager.versionCode
    }

    //endregion
}
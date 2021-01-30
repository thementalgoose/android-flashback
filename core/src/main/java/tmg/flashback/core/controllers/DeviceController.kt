package tmg.flashback.core.controllers

import org.threeten.bp.LocalDate
import tmg.flashback.core.managers.BuildConfigManager
import tmg.flashback.core.repositories.CoreRepository

/**
 * Controller to handle device prefs and actions that
 *  may require perminent storage
 */
class DeviceController(
    private val coreRepository: CoreRepository,
    private val buildConfigManager: BuildConfigManager
) {
    //region Shake to report

    var shakeToReport: Boolean
        get() = coreRepository.shakeToReport
        set(value) {
            coreRepository.shakeToReport = value
        }

    //endregion

    /**
     * To be ran when the app is first opened
     */
    fun appOpened() {
        appOpenedCount += 1
        updateAppVersion()
    }

    var appOpenedCount: Int
        get() = coreRepository.appOpenedCount
        private set(value) {
            coreRepository.appOpenedCount = value
        }

    val appFirstBoot: LocalDate
        get() = coreRepository.appFirstBootTime

    //region Device UDID

    val deviceUdid: String
        get() = coreRepository.deviceUdid

    //endregion

    //region Last app version

    var lastAppVersion: Int
        get() = coreRepository.lastAppVersion
        private set(value) {
            coreRepository.lastAppVersion = value
        }

    private fun updateAppVersion() {
        lastAppVersion = buildConfigManager.versionCode
    }

    //endregion
}
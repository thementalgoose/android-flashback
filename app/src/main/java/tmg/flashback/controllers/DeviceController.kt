package tmg.flashback.controllers

import org.threeten.bp.LocalDate
import tmg.flashback.BuildConfig
import tmg.flashback.repo.pref.DeviceRepository
import java.lang.RuntimeException
import java.util.*

/**
 * Controller to handle device prefs and actions that
 *  may require perminent storage
 */
class DeviceController(
        private val deviceRepository: DeviceRepository
) {
    //region Shake to report

    var shakeToReport: Boolean
        get() = deviceRepository.shakeToReport
        set(value) {
            deviceRepository.shakeToReport = value
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
        get() = deviceRepository.appOpenedCount
        private set(value) {
            deviceRepository.appOpenedCount = value
        }

    val appFirstBoot: LocalDate
        get() = deviceRepository.appFirstBootTime

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
        lastAppVersion = BuildConfig.VERSION_CODE
    }

    //endregion
}
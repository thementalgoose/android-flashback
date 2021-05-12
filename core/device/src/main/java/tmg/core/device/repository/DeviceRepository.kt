package tmg.core.device.repository

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.core.prefs.manager.PreferenceManager
import java.util.*

class DeviceRepository(
    private val sharedPreferenceRepository: PreferenceManager
) {
    companion object {
        private const val keyAppOpenedCount: String = "APP_STARTUP_OPEN_COUNT"
        private const val keyAppFirstBoot: String = "APP_STARTUP_FIRST_BOOT"
        private const val keyDeviceUDID: String = "UDID"
        private const val keyAppVersion: String = "RELEASE_NOTES" // Used to be release notes
    }

    private val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

    /**
     * How many times the application onCreate has been called
     */
    var appOpenedCount: Int
        get() = sharedPreferenceRepository.getInt(keyAppOpenedCount, 0)
        set(value) = sharedPreferenceRepository.save(keyAppOpenedCount, value)

    /**
     * LocalDate of when the app was first opened
     */
    var appFirstOpened: LocalDate
        get() {
            val value = sharedPreferenceRepository.getString(keyAppFirstBoot, null)
            if (value == null) {
                val result = LocalDate.now()
                appFirstOpened = result
                return result
            }
            return LocalDate.parse(value, dateFormat)
        }
        set(value) = sharedPreferenceRepository.save(keyAppFirstBoot, value.format(dateFormat))

    /**
     * Last app version that was opened
     */
    var lastAppVersion: Int
        get() = sharedPreferenceRepository.getInt(keyAppVersion, 0)
        set(value) = sharedPreferenceRepository.save(keyAppVersion, value)

    /**
     * Randomly generated device UDID to uniquely identify app install session
     */
    var deviceUdid: String
        set(value) = sharedPreferenceRepository.save(keyDeviceUDID, value)
        get() {
            var key = sharedPreferenceRepository.getString(keyDeviceUDID, "")
            if (key.isNullOrEmpty()) {
                val newKey = UUID.randomUUID().toString()
                deviceUdid = newKey
                key = newKey
            }
            return key
        }
}
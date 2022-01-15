package tmg.flashback.device.repository

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException
import tmg.flashback.prefs.manager.PreferenceManager
import java.util.*

class DeviceRepository(
    private val preferenceManager: PreferenceManager
) {
    companion object {
        private const val keyAppOpenedCount: String = "APP_STARTUP_OPEN_COUNT"
        private const val keyAppFirstBoot: String = "APP_STARTUP_FIRST_BOOT"
        private const val keyDeviceUDID: String = "UDID"
        private const val keyAppVersion: String = "RELEASE_NOTES" // Used to be release notes
    }

    private val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.ENGLISH)

    /**
     * How many times the application onCreate has been called
     */
    var appOpenedCount: Int
        get() = preferenceManager.getInt(keyAppOpenedCount, 0)
        set(value) = preferenceManager.save(keyAppOpenedCount, value)

    /**
     * LocalDate of when the app was first opened
     */
    var appFirstOpened: LocalDate
        get() {
            val value = preferenceManager.getString(keyAppFirstBoot, null)
            if (value == null) {
                val result = LocalDate.now()
                appFirstOpened = result
                return result
            }
            return try {
                LocalDate.parse(value, dateFormat)
            } catch (e: DateTimeParseException) {
                val result = LocalDate.now()
                appFirstOpened = result
                result
            }
        }
        set(value) = preferenceManager.save(keyAppFirstBoot, value.format(dateFormat))

    /**
     * Last app version that was opened
     */
    var lastAppVersion: Int
        get() = preferenceManager.getInt(keyAppVersion, 0)
        set(value) = preferenceManager.save(keyAppVersion, value)

    /**
     * Randomly generated device UDID to uniquely identify app install session
     */
    var deviceUdid: String
        set(value) = preferenceManager.save(keyDeviceUDID, value)
        get() {
            var key = preferenceManager.getString(keyDeviceUDID, "")
            if (key.isNullOrEmpty()) {
                val newKey = UUID.randomUUID().toString()
                deviceUdid = newKey
                key = newKey
            }
            return key
        }
}
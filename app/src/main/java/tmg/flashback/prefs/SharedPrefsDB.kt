package tmg.flashback.prefs

import android.content.Context
import tmg.flashback.BuildConfig
import tmg.flashback.currentYear
import tmg.flashback.releaseNotes
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.enums.ThemePref
import tmg.flashback.repo.enums.ViewTypePref
import tmg.utilities.extensions.toEnum
import tmg.utilities.prefs.SharedPrefManager
import tmg.utilities.utils.SharedPreferencesUtils
import java.util.*

class SharedPrefsDB(context: Context): SharedPrefManager(context), PrefsDB {

    override val prefsKey: String = "Flashback"
    private val keySelectedYear: String = "SELECTED_YEAR"
    private val keyShowQualifyingDelta: String = "SHOW_QUALIFYING_DELTA"
    private val keyViewType: String = "VIEW_TYPE"
    private val keyCrashReporting: String = "CRASH_REPORTING"
    private val keyShakeToReport: String = "SHAKE_TO_REPORT"
    private val keyReleaseNotes: String = "RELEASE_NOTES"
    private val keyDeviceUDID: String = "UDID"
    private val keyTheme: String = "THEME"

    override var theme: ThemePref
        get() = ThemePref.DAY
        set(value) {
            // TODO
        }

    override var selectedYear: Int
        get() = getInt(keySelectedYear, currentYear)
        set(value) {
            save(keySelectedYear, value)
        }

    override var showQualifyingDelta: Boolean
        get() = getBoolean(keyShowQualifyingDelta)
        set(value) {
            save(keyShowQualifyingDelta, value)
        }

    override var viewType: ViewTypePref
        get() = getString(keyViewType, ViewTypePref.STATIC.key)?.toEnum<ViewTypePref> { it.key } ?: ViewTypePref.STATIC
        set(value) {
            save(keyViewType, value.key)
        }

    override var crashReporting: Boolean
        get() = getBoolean(keyCrashReporting, true)
        set(value) {
            save(keyCrashReporting, value)
        }

    override var shakeToReport: Boolean
        get() = getBoolean(keyShakeToReport, true)
        set(value) {
            save(keyShakeToReport, value)
        }
    override var lastAppVersion: Int
        get() = getInt(keyReleaseNotes, 0)
        set(value) {
            save(keyReleaseNotes, value)
        }

    override val shouldShowReleaseNotes: Boolean
        get() = BuildConfig.VERSION_CODE > lastAppVersion && releaseNotes.keys.count { it > lastAppVersion} > 0

    override var deviceUdid: String
        set(value) {
            save(keyDeviceUDID, value)
        }
        get() {
            var key = getString(keyDeviceUDID, "")
            if (key.isNullOrEmpty()) {
                val newKey = UUID.randomUUID().toString()
                deviceUdid = newKey
                key = newKey
            }
            return key
        }
}
package tmg.f1stats.prefs

import android.content.Context
import org.threeten.bp.Year
import org.threeten.bp.ZoneId
import tmg.f1stats.BuildConfig
import tmg.f1stats.currentYear
import tmg.f1stats.repo.db.PrefsDB
import tmg.f1stats.repo.enums.ThemePref
import tmg.f1stats.repo.enums.ViewTypePref
import tmg.utilities.extensions.toEnum
import tmg.utilities.utils.SharedPreferencesUtils
import java.util.*

class SharedPrefsDB(val context: Context) : PrefsDB {

    private val prefsKey: String = "F1Stats"
    private val keySelectedYear: String = "SELECTED_YEAR"
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
            value.save(keySelectedYear)
        }

    override var viewType: ViewTypePref
        get() = getString(keyViewType, ViewTypePref.STATIC.key).toEnum<ViewTypePref> { it.key } ?: ViewTypePref.STATIC
        set(value) {
            value.key.save(keyViewType)
        }

    override var crashReporting: Boolean
        get() = getBoolean(keyCrashReporting)
        set(value) {
            value.save(keyCrashReporting)
        }

    override var shakeToReport: Boolean
        get() = getBoolean(keyShakeToReport)
        set(value) {
            value.save(keyShakeToReport)
        }
    override var lastAppVersion: Int
        get() = getInt(keyReleaseNotes, 0)
        set(value) {
            value.save(keyReleaseNotes)
        }

    override val isCurrentAppVersionNew: Boolean
        get() = BuildConfig.VERSION_CODE > lastAppVersion

    override var deviceUdid: String
        set(value) {
            value.save(keyDeviceUDID)
        }
        get() {
            var key = getString(keyDeviceUDID, "")
            if (key.isEmpty()) {
                val newKey = UUID.randomUUID().toString()
                deviceUdid = newKey
                key = newKey
            }
            return key
        }

    //region Utils

    private fun Int.save(key: String) {
        SharedPreferencesUtils.save(context, key, this, prefsKey)
    }

    private fun Boolean.save(key: String) {
        SharedPreferencesUtils.save(context, key, this, prefsKey)
    }

    private fun String.save(key: String) {
        SharedPreferencesUtils.save(context, key, this, prefsKey)
    }

    private fun getInt(key: String, default: Int = -1): Int {
        val value = SharedPreferencesUtils.getInt(context, key, prefsKey = prefsKey)
        return if (value == -1) default
        else value
    }


    private fun getString(key: String, default: String): String {
        return SharedPreferencesUtils.getString(context, key, default, prefsKey = prefsKey)
    }

    private fun getBoolean(key: String): Boolean {
        return SharedPreferencesUtils.getBoolean(context, key, prefsKey = prefsKey)
    }

    //endregion
}
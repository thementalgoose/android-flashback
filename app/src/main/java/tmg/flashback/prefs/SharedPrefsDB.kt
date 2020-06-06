package tmg.flashback.prefs

import android.content.Context
import tmg.flashback.BuildConfig
import tmg.flashback.releaseNotes
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.enums.ThemePref
import tmg.flashback.repo.enums.ViewTypePref
import tmg.utilities.extensions.toEnum
import tmg.utilities.prefs.SharedPrefManager
import tmg.utilities.utils.SharedPreferencesUtils
import java.util.*

private const val defaultShowQualifying: Boolean = false
private const val defaultShowDriversInConstructors: Boolean = true
private const val defaultShakeToReport: Boolean = true
private const val defaultCrashReporting: Boolean = true

class SharedPrefsDB(context: Context): SharedPrefManager(context), PrefsDB {

    override val prefsKey: String = "Flashback"
    private val keySelectedYear: String = "SELECTED_YEAR"
    private val keyShowQualifyingDelta: String = "SHOW_QUALIFYING_DELTA"
    private val keyShowDriversInConstructorStandings: String = "SHOW_DRIVERS_IN_CONSTRUCTOR_STANDINGS"
    private val keyViewType: String = "VIEW_TYPE"
    private val keyCrashReporting: String = "CRASH_REPORTING"
    private val keyShakeToReport: String = "SHAKE_TO_REPORT"
    private val keyReleaseNotes: String = "RELEASE_NOTES"
    private val keyDeviceUDID: String = "UDID"
    private val keyTheme: String = "THEME"
    private val keyFavouriteSeasons: String = "FAVOURITE_SEASONS"

    override var theme: ThemePref
        get() = getString(keyTheme)?.toEnum<ThemePref> { it.key } ?: ThemePref.AUTO
        set(value) = save(keyTheme, value.key)

    override var showQualifyingDelta: Boolean
        get() = getBoolean(keyShowQualifyingDelta, defaultShowQualifying)
        set(value) = save(keyShowQualifyingDelta, value)

    override var showDriversBehindConstructor: Boolean
        get() = getBoolean(keyShowDriversInConstructorStandings, defaultShowDriversInConstructors)
        set(value) = save(keyShowDriversInConstructorStandings, value)

    override var viewType: ViewTypePref
        get() = getString(keyViewType, ViewTypePref.STATIC.key)?.toEnum<ViewTypePref> { it.key } ?: ViewTypePref.STATIC
        set(value) = save(keyViewType, value.key)

    override var crashReporting: Boolean
        get() = getBoolean(keyCrashReporting, defaultShakeToReport)
        set(value) = save(keyCrashReporting, value)

    override var shakeToReport: Boolean
        get() = getBoolean(keyShakeToReport, defaultCrashReporting)
        set(value) = save(keyShakeToReport, value)

    override var lastAppVersion: Int
        get() = getInt(keyReleaseNotes, 0)
        set(value) = save(keyReleaseNotes, value)

    override val shouldShowReleaseNotes: Boolean
        get() {
            return if (lastAppVersion == 0) {
                lastAppVersion = BuildConfig.VERSION_CODE
                false
            } else {
                BuildConfig.VERSION_CODE > lastAppVersion && releaseNotes.keys.count { it > lastAppVersion} > 0
            }
        }

    override var deviceUdid: String
        set(value) = save(keyDeviceUDID, value)
        get() {
            var key = getString(keyDeviceUDID, "")
            if (key.isNullOrEmpty()) {
                val newKey = UUID.randomUUID().toString()
                deviceUdid = newKey
                key = newKey
            }
            return key
        }

    override var favouriteSeasons: Set<Int>
        set(value) = save(keyFavouriteSeasons, value.map { it.toString() }.toSet())
        get() {
            val value = getSet(keyFavouriteSeasons, setOf())
            return value
                .mapNotNull { it.toIntOrNull() }
                .toSet()
        }
}
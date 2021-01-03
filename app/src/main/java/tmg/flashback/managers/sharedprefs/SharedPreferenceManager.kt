package tmg.flashback.managers.sharedprefs

import android.content.Context
import android.os.Build
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.BuildConfig
import tmg.flashback.constants.Defaults
import tmg.flashback.releaseNotes
import tmg.flashback.repo.enums.AppHints
import tmg.flashback.repo.pref.UserRepository
import tmg.flashback.repo.enums.BarAnimation
import tmg.flashback.repo.enums.NotificationRegistration
import tmg.flashback.repo.enums.ThemePref
import tmg.flashback.repo.pref.DeviceRepository
import tmg.flashback.rss.prefs.RSSPrefsRepository
import tmg.utilities.extensions.toEnum
import tmg.utilities.prefs.SharedPrefManager
import java.util.*

class SharedPreferenceManager(context: Context) : SharedPrefManager(context),
        UserRepository,
        DeviceRepository,
        RSSPrefsRepository {

    override val prefsKey: String = "Flashback"
    private val keyShowQualifyingDelta: String = "SHOW_QUALIFYING_DELTA"
    private val keyFadeDNF: String = "FADE_DNF"
    private val keyShowGridPenaltiesInQualifying: String = "SHOW_GRID_PENALTIES_IN_QUALIFYING"
    private val keyBottomSheetAll: String = "BOTTOM_SHEET_ALL"
    private val keyBottomSheetFavourited: String = "BOTTOM_SHEET_FAVOURITED"
    private val keyBarAnimation: String = "BAR_ANIMATION"
    private val keyCrashReporting: String = "CRASH_REPORTING"
    private val keyShakeToReport: String = "SHAKE_TO_REPORT"
    private val keyReleaseNotes: String = "RELEASE_NOTES"
    private val keyDeviceUDID: String = "UDID"
    private val keyTheme: String = "THEME"
    private val keyFavouriteSeasons: String = "FAVOURITE_SEASONS"
    private val keyRSSList: String = "RSS_LIST"
    private val keyNewsOpenInExternalBrowser: String = "NEWS_OPEN_IN_EXTERNAL_BROWSER"
    private val keyInAppEnableJavascript: String = "IN_APP_ENABLE_JAVASCRIPT"
    private val keyNewsShowDescription: String = "NEWS_SHOW_DESCRIPTIONS"
    private val keyAppHints: String = "APP_HINTS"
    private val keyDefaultSeason: String = "DEFAULT_SEASON"

    private val keyNotificationRace: String = "NOTIFICATION_RACE"
    private val keyNotificationQualifying: String = "NOTIFICATION_QUALIFYING"
    private val keyNotificationMisc: String = "NOTIFICATION_MISC"

    private val keyAppFirstBoot: String = "APP_STARTUP_FIRST_BOOT"
    private val keyAppOpenCount: String = "APP_STARTUP_OPEN_COUNT"

    private val keyRemoteConfigInitialSync: String = "REMOTE_CONFIG_INITIAL_SYNC"



    private val dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd")

    //region Customise

    override var showQualifyingDelta: Boolean
        get() = getBoolean(keyShowQualifyingDelta, Defaults.showQualifyingDelta)
        set(value) = save(keyShowQualifyingDelta, value)

    override var fadeDNF: Boolean
        get() = getBoolean(keyFadeDNF, Defaults.fadeDNF)
        set(value) = save(keyFadeDNF, value)

    override var showListFavourited: Boolean
        get() = getBoolean(keyBottomSheetFavourited, Defaults.showListFavourited)
        set(value) = save(keyBottomSheetFavourited, value)

    override var showListAll: Boolean
        get() = getBoolean(keyBottomSheetAll, Defaults.showListAll)
        set(value) = save(keyBottomSheetAll, value)

    override var barAnimation: BarAnimation
        get() = getString(keyBarAnimation)?.toEnum<BarAnimation> { it.key } ?: Defaults.barAnimation
        set(value) = save(keyBarAnimation, value.key)

    override var showGridPenaltiesInQualifying: Boolean
        get() = getBoolean(keyShowGridPenaltiesInQualifying, Defaults.showGridPenaltiesInQualifying)
        set(value) = save(keyShowGridPenaltiesInQualifying, value)

    override var favouriteSeasons: Set<Int>
        set(value) = save(keyFavouriteSeasons, value.map { it.toString() }.toSet())
        get() {
            val value = getSet(keyFavouriteSeasons, setOf())
            return value
                    .mapNotNull { it.toIntOrNull() }
                    .toSet()
        }

    override var theme: ThemePref
        get() = getString(keyTheme)?.toEnum<ThemePref> { it.key } ?: Defaults.theme
        set(value) = save(keyTheme, value.key)

    override var appHints: Set<AppHints>
        set(value) = save(keyAppHints, value.map { it.id }.toSet())
        get() {
            val value = getSet(keyAppHints, setOf())
            return value
                .mapNotNull { id -> id.toEnum<AppHints> { it.id } }
                .toSet()
        }

    override var defaultSeason: Int?
        get() {
            val value = getInt(keyDefaultSeason, -1)
            if (value == -1) return null
            return value
        }
        set(value) {
            val valueToSave = when (value) {
                null -> -1
                else -> value
            }
            save(keyDefaultSeason, valueToSave)
        }

    //endregion



    //region Device

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

    override var crashReporting: Boolean
        get() = getBoolean(keyCrashReporting, Defaults.crashReporting)
        set(value) = save(keyCrashReporting, value)

    override var shakeToReport: Boolean
        get() = getBoolean(keyShakeToReport, Defaults.shakeToReport)
        set(value) = save(keyShakeToReport, value)

    override var lastAppVersion: Int
        get() = getInt(keyReleaseNotes, 0)
        set(value) = save(keyReleaseNotes, value)

    override var appFirstBootTime: LocalDate
        get() {
            val value = getString(keyAppFirstBoot, null)
            if (value == null) {
                val result = LocalDate.now()
                save(keyAppFirstBoot, result.format(dateFormat))
                return result
            }
            return LocalDate.parse(value, dateFormat)
        }
        set(value) = save(keyAppFirstBoot, value.format(dateFormat))

    override var appOpenedCount: Int
        get() = getInt(keyAppOpenCount, 0)
        set(value) = save(keyAppOpenCount, value)

    override var remoteConfigInitialSync: Boolean
        get() = getBoolean(keyRemoteConfigInitialSync, false)
        set(value) = save(keyRemoteConfigInitialSync, value)

    //endregion



    //region Notifications

    override var notificationsQualifying: NotificationRegistration?
        get() = getString(keyNotificationQualifying, null)?.toEnum<NotificationRegistration>()
        set(value) = if (value != null) {
            save(keyNotificationQualifying, value.key)
        } else {
            save(keyNotificationQualifying, "")
        }

    override var notificationsRace: NotificationRegistration?
        get() = getString(keyNotificationRace, null)?.toEnum<NotificationRegistration>()
        set(value) = if (value != null) {
            save(keyNotificationRace, value.key)
        } else {
            save(keyNotificationRace, "")
        }

    override var notificationsMisc: NotificationRegistration?
        get() = getString(keyNotificationMisc, null)?.toEnum<NotificationRegistration>()
        set(value) = if (value != null) {
            save(keyNotificationMisc, value.key)
        } else {
            save(keyNotificationMisc, "")
        }

    //endregion



    //region RSS

    override var rssUrls: Set<String>
        set(value) = save(keyRSSList, value.toSet())
        get() = getSet(keyRSSList, setOf())

    override var inAppEnableJavascript: Boolean
        get() = getBoolean(keyInAppEnableJavascript, true)
        set(value) = save(keyInAppEnableJavascript, value)

    override var rssShowDescription: Boolean
        get() = getBoolean(keyNewsShowDescription, true)
        set(value) = save(keyNewsShowDescription, value)

    override var newsOpenInExternalBrowser: Boolean
        get() = getBoolean(keyNewsOpenInExternalBrowser, false)
        set(value) = save(keyNewsOpenInExternalBrowser, value)

    //endregion
}
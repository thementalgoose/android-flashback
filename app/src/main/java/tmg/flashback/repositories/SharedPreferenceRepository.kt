package tmg.flashback.repositories

import android.content.Context
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.constants.Defaults
import tmg.flashback.core.enums.AnimationSpeed
import tmg.flashback.core.enums.AppHints
import tmg.flashback.core.enums.Theme
import tmg.flashback.core.repositories.CoreRepository
import tmg.flashback.data.repositories.AppRepository
import tmg.flashback.data.enums.NotificationRegistration
import tmg.flashback.rss.prefs.RSSRepository
import tmg.utilities.extensions.toEnum
import tmg.utilities.prefs.SharedPrefManager
import java.util.*

class SharedPreferenceRepository(context: Context) : SharedPrefManager(context),
    AppRepository,
    CoreRepository,
    RSSRepository {

    override val prefsKey: String = "Flashback"
    private val keyShowQualifyingDelta: String = "SHOW_QUALIFYING_DELTA"
    private val keyFadeDNF: String = "FADE_DNF"
    private val keyShowGridPenaltiesInQualifying: String = "SHOW_GRID_PENALTIES_IN_QUALIFYING"
    private val keyBottomSheetAll: String = "BOTTOM_SHEET_ALL"
    private val keyBottomSheetFavourited: String = "BOTTOM_SHEET_FAVOURITED"
    private val keyAnimationSpeed: String = "BAR_ANIMATION"
    private val keyCrashReporting: String = "CRASH_REPORTING"
    private val keyShakeToReport: String = "SHAKE_TO_REPORT"
    private val keyAnalyticsOptIn: String = "ANALYTICS_OPT_IN"
    private val keyAppVersion: String = "RELEASE_NOTES" // Used to be release notes
    private val keyDeviceUDID: String = "UDID"
    private val keyTheme: String = "THEME"
    private val keyFavouriteSeasons: String = "FAVOURITE_SEASONS"
    private val keyRSSList: String = "RSS_LIST"
    private val keyNewsOpenInExternalBrowser: String = "NEWS_OPEN_IN_EXTERNAL_BROWSER"
    private val keyInAppEnableJavascript: String = "IN_APP_ENABLE_JAVASCRIPT"
    private val keyNewsShowDescription: String = "NEWS_SHOW_DESCRIPTIONS"
    private val keyAppHints: String = "APP_HINTS"
    private val keyDefaultSeason: String = "DEFAULT_SEASON"
    private val keyReleaseNotesSeenVersion: String = "RELEASE_NOTES_SEEN_VERSION"

    private val keyNotificationRace: String = "NOTIFICATION_RACE"
    private val keyNotificationQualifying: String = "NOTIFICATION_QUALIFYING"
    private val keyNotificationSeasonInfo: String = "NOTIFICATION_SEASON_INFO"

    private val keyAppFirstBoot: String = "APP_STARTUP_FIRST_BOOT"
    private val keyAppOpenCount: String = "APP_STARTUP_OPEN_COUNT"

    private val keyRemoteConfigSyncCount: String = "REMOTE_CONFIG_SYNC_COUNT"

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

    override var animationSpeed: AnimationSpeed
        get() = getString(keyAnimationSpeed)?.toEnum<AnimationSpeed> { it.key } ?: Defaults.animationSpeed
        set(value) = save(keyAnimationSpeed, value.key)

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

    override var theme: Theme
        get() = getString(keyTheme)?.toEnum<Theme> { it.key } ?: Defaults.theme
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
    override var remoteConfigSync: Int
        get() = getInt(keyRemoteConfigSyncCount, 0)
        set(value) = save(keyRemoteConfigSyncCount, value)

    override var crashReporting: Boolean
        get() = getBoolean(keyCrashReporting, Defaults.crashReporting)
        set(value) = save(keyCrashReporting, value)

    override var shakeToReport: Boolean
        get() = getBoolean(keyShakeToReport, Defaults.shakeToReport)
        set(value) = save(keyShakeToReport, value)

    override var analytics: Boolean
        get() = getBoolean(keyAnalyticsOptIn, Defaults.analyticsOptIn)
        set(value) = save(keyAnalyticsOptIn, value)

    override var lastAppVersion: Int
        get() = getInt(keyAppVersion, 0)
        set(value) = save(keyAppVersion, value)

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

    override var releaseNotesSeenAppVersion: Int
        get() = getInt(keyReleaseNotesSeenVersion, 0)
        set(value) = save(keyReleaseNotesSeenVersion, value)

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

    override var notificationsSeasonInfo: NotificationRegistration?
        get() = getString(keyNotificationSeasonInfo, null)?.toEnum<NotificationRegistration>()
        set(value) = if (value != null) {
            save(keyNotificationSeasonInfo, value.key)
        } else {
            save(keyNotificationSeasonInfo, "")
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
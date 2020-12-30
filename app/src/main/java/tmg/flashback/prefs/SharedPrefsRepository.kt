package tmg.flashback.prefs

import android.content.Context
import android.os.Build
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.BuildConfig
import tmg.flashback.releaseNotes
import tmg.flashback.repo.pref.PrefCustomisationRepository
import tmg.flashback.repo.enums.BarAnimation
import tmg.flashback.repo.enums.NotificationRegistration
import tmg.flashback.repo.enums.ThemePref
import tmg.flashback.repo.pref.PrefDeviceRepository
import tmg.flashback.repo.pref.PrefNotificationRepository
import tmg.flashback.rss.prefs.RSSPrefsRepository
import tmg.utilities.extensions.toEnum
import tmg.utilities.prefs.SharedPrefManager
import java.util.*

private const val defaultShowQualifying: Boolean = false
private const val defaultShakeToReport: Boolean = true
private const val defaultCrashReporting: Boolean = true

class SharedPrefsRepository(context: Context) : SharedPrefManager(context),
        PrefCustomisationRepository,
        PrefDeviceRepository,
        PrefNotificationRepository,
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

    private val keyNotificationRace: String = "NOTIFICATION_RACE"
    private val keyNotificationQualifying: String = "NOTIFICATION_QUALIFYING"
    private val keyNotificationMisc: String = "NOTIFICATION_MISC"

    private val keyAppFirstBoot: String = "APP_STARTUP_FIRST_BOOT"
    private val keyAppOpenCount: String = "APP_STARTUP_OPEN_COUNT"

    private val keyRemoteConfigInitialSync: String = "REMOTE_CONFIG_INITIAL_SYNC"



    private val dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd")

    //region Customise

    override var showQualifyingDelta: Boolean
        get() = getBoolean(keyShowQualifyingDelta, defaultShowQualifying)
        set(value) = save(keyShowQualifyingDelta, value)

    override var fadeDNF: Boolean
        get() = getBoolean(keyFadeDNF, true)
        set(value) = save(keyFadeDNF, value)

    override var showBottomSheetFavourited: Boolean
        get() = getBoolean(keyBottomSheetFavourited, true)
        set(value) = save(keyBottomSheetFavourited, value)

    override var showBottomSheetAll: Boolean
        get() = getBoolean(keyBottomSheetAll, true)
        set(value) = save(keyBottomSheetAll, value)

    override var barAnimation: BarAnimation
        get() = getString(keyBarAnimation)?.toEnum<BarAnimation> { it.key } ?: BarAnimation.MEDIUM
        set(value) = save(keyBarAnimation, value.key)

    override var showGridPenaltiesInQualifying: Boolean
        get() = getBoolean(keyShowGridPenaltiesInQualifying, true)
        set(value) = save(keyShowGridPenaltiesInQualifying, value)

    override val shouldShowReleaseNotes: Boolean
        get() {
            return if (lastAppVersion == 0) {
                lastAppVersion = BuildConfig.VERSION_CODE
                false
            } else {
                BuildConfig.VERSION_CODE > lastAppVersion && releaseNotes.keys.count { it > lastAppVersion } > 0
            }
        }

    override var favouriteSeasons: Set<Int>
        set(value) = save(keyFavouriteSeasons, value.map { it.toString() }.toSet())
        get() {
            val value = getSet(keyFavouriteSeasons, setOf())
            return value
                    .mapNotNull { it.toIntOrNull() }
                    .toSet()
        }

    override var theme: ThemePref
        get() = getString(keyTheme)?.toEnum<ThemePref> { it.key } ?: ThemePref.AUTO
        set(value) = save(keyTheme, value.key)

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
        get() = getBoolean(keyCrashReporting, defaultShakeToReport)
        set(value) = save(keyCrashReporting, value)

    override var shakeToReport: Boolean
        get() = getBoolean(keyShakeToReport, defaultCrashReporting)
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

    override val isNotificationChannelsSupported: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

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
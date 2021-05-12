package tmg.flashback.repositories

import android.content.Context
import org.threeten.bp.format.DateTimeFormatter
import tmg.configuration.repository.models.TimeListDisplayType
import tmg.flashback.constants.Defaults
import tmg.core.ui.model.AnimationSpeed
import tmg.flashback.core.enums.AppHints
import tmg.flashback.core.repositories.CoreRepository
import tmg.flashback.data.repositories.AppRepository
import tmg.flashback.rss.prefs.RSSRepositoryI
import tmg.utilities.extensions.toEnum
import tmg.utilities.prefs.SharedPrefManager
import java.util.*

class SharedPreferenceRepository(context: Context) : SharedPrefManager(context),
    AppRepository,
    CoreRepository,
    RSSRepositoryI {

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
    private val keyTimeDisplayListType: String = "TIME_DISPLAY_LIST_TYPE"
    private val keyWidgetOpenBehavior: String = "WIDGET_OPEN_BEHAVIOR"

    private val keyNotificationRace: String = "NOTIFICATION_RACE"
    private val keyNotificationQualifying: String = "NOTIFICATION_QUALIFYING"
    private val keyNotificationSeasonInfo: String = "NOTIFICATION_SEASON_INFO"

    private val keyAppFirstBoot: String = "APP_STARTUP_FIRST_BOOT"
    private val keyAppOpenCount: String = "APP_STARTUP_OPEN_COUNT"

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

    override var displayListTypePref: TimeListDisplayType
        get() = getInt(keyTimeDisplayListType).toEnum<TimeListDisplayType>() ?: TimeListDisplayType.RELATIVE
        set(value) = save(keyTimeDisplayListType, value.ordinal)

    override var widgetOpenApp: Boolean
        get() = getBoolean(keyWidgetOpenBehavior, Defaults.widgetOpenApp)
        set(value) = save(keyWidgetOpenBehavior, value)

    //endregion



    //region Device

    override var releaseNotesSeenAppVersion: Int
        get() = getInt(keyReleaseNotesSeenVersion, 0)
        set(value) = save(keyReleaseNotesSeenVersion, value)

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
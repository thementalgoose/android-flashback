package tmg.flashback

import android.content.Context
import androidx.annotation.DrawableRes
import org.threeten.bp.Year
import tmg.components.about.AboutThisAppConfiguration
import tmg.components.about.AboutThisAppDependency
import tmg.flashback.utils.SeasonRound

/**
 * Constants
 */
const val minimumSupportedYear = 1950
val currentYear: Int
    get() = Year.now().value
val allYears: List<Int>
    get() = (minimumSupportedYear..currentYear).map { it }

/**
 * Play store market url
 */
const val playStoreUrl: String = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID

/**
 * Days until the app banner gets displayed at the bottom
 */
const val daysUntilDataProvidedBannerMovedToBottom = 5

/**
 * Coming soon message for race statistics that are not synchronised
 */
const val showComingSoonMessageForNextDays = 10

/**
 * Bottom sheet fast scroll
 */
const val bottomSheetFastScrollDuration = 300

/**
 * When did championships start
 */
const val constructorChampionshipStarts = 1958

/**
 * Max points by season
 */
fun maxPointsBySeason(season: Int): Int {
    return when {
        season >= 2010 -> 25
        season >= 1991 -> 10
        else -> 8
    }
}

/**
 * Compile time feature toggles
 */
const val showDriverSummary: Boolean = false

/**
 * Colours for the decades for the menu
 */
val coloursDecade: Map<String, String> = mapOf(
        "1950" to "#9fa8da",
        "1960" to "#80deea",
        "1970" to "#ef9a9a",
        "1980" to "#90caf9",
        "1990" to "#a5d6a7",
        "2000" to "#81d4fa",
        "2010" to "#ce93d8",
        "2020" to "#f48fb1",
        "2030" to "#b39ddb",
        "2040" to "#c5e1a5",
        "2050" to "#80cbc4",
        "2060" to "#b0bec5",
        "2070" to "#ffcc80",
        "2080" to "#ffab91",
        "2090" to "#b0bec5"
)
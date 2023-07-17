package tmg.flashback.formula1.constants

import androidx.compose.ui.graphics.Color
import org.threeten.bp.Year

object Formula1 {

    /**
     * Current Year season
     */
    val currentSeasonYear: Int
        get() = Year.now().value

    /**
     * What season should the API have all the data up too
     */
    const val championshipDataCompleteUpTo: Int = 2022

    /**
     * When did F1 start
     */
    const val championshipStarted = 1950

    /**
     * Qualifying data available from
     */
    const val qualifyingDataAvailableFrom: Int = 1995

    /**
     * When did championships start
     */
    const val championshipConstructorStarts = 1958

    /**
     * Sprint race began
     */
    const val sprintsIntroducedIn = 2021

    /**
     * Maximum points awarded to a driver in a single race based on the
     * season
     */
    fun maxDriverPointsBySeason(season: Int): Int {
        return when {
            season >= 2010 -> 26
            season >= 1991 -> 11
            else -> 8
        }
    }
    fun maxTeamPointsBySeason(season: Int): Int {
        return when {
            season >= 2022 -> 60
            season >= 2021 -> 47
            season >= 2010 -> 42
            season >= 1991 -> 19
            else -> 14
        }
    }

    val decadeColours: Map<String, Color> = mapOf(
        "1950" to Color(0xFF9FA8DA),
        "1960" to Color(0xFF80DEEA),
        "1970" to Color(0xFFEF9A9A),
        "1980" to Color(0xFF90CAF9),
        "1990" to Color(0xFFA5D6A7),
        "2000" to Color(0xFF81D4FA),
        "2010" to Color(0xFFCE93D8),
        "2020" to Color(0xFFF48FB1),
        "2030" to Color(0xFFB39DDB),
        "2040" to Color(0xFFC5E1A5),
        "2050" to Color(0xFF80CBC4),
        "2060" to Color(0xFFB0BEC5),
        "2070" to Color(0xFFFFCC80),
        "2080" to Color(0xFFFFAB91),
        "2090" to Color(0xFFB0BEC5)
    )
}

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
    val allDataUpToo: Int = 2021

    /**
     * When did F1 start
     */
    const val minimumSupportedYear = 1950

    /**
     * When did championships start
     */
    const val constructorChampionshipStarts = 1958

    /**
     * Coming soon message for race statistics that are not synchronised or an error has occurred for
     */
    const val showComingSoonMessageForNextDays = 10

    /**
     * Maximum points awarded to a driver in a single race based on the
     * season
     */
    fun maxPointsBySeason(season: Int): Int {
        return when {
            season >= 2021 -> 28
            season >= 2010 -> 25
            season >= 1991 -> 10
            else -> 8
        }
    }

    val decadeColours: Map<String, Color> = mapOf(
        "1950" to Color(0xFF9fa8da),
        "1960" to Color(0xFF80deea),
        "1970" to Color(0xFFef9a9a),
        "1980" to Color(0xFF90caf9),
        "1990" to Color(0xFFa5d6a7),
        "2000" to Color(0xFF81d4fa),
        "2010" to Color(0xFFce93d8),
        "2020" to Color(0xFFf48fb1),
        "2030" to Color(0xFFb39ddb),
        "2040" to Color(0xFFc5e1a5),
        "2050" to Color(0xFF80cbc4),
        "2060" to Color(0xFFb0bec5),
        "2070" to Color(0xFFffcc80),
        "2080" to Color(0xFFffab91),
        "2090" to Color(0xFFb0bec5)
    )

    /**
     * Colours for the decades for the menu
     */
    @Deprecated("Please use decadeColours")
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
}

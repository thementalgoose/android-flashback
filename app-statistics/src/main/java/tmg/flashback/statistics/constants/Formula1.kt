package tmg.flashback.statistics.constants

import org.threeten.bp.Year

object Formula1 {

    /**
     * Current Year season
     */
    val currentSeasonYear: Int
        get() = Year.now().value

    /**
     * When did F1 start
     */
    const val minimumSupportedYear = 1950

    /**
     * When did championships start
     */
    const val constructorChampionshipStarts = 1958

    /**
     * Maximum points awarded to a driver in a given season
     */
    fun maxPointsBySeason(season: Int): Int {
        return when {
            season >= 2010 -> 25
            season >= 1991 -> 10
            else -> 8
        }
    }
}
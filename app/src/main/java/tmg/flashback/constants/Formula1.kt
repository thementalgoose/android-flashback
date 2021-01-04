package tmg.flashback.constants

object Formula1 {

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
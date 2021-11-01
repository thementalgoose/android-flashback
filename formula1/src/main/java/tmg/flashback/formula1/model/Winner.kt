package tmg.flashback.formula1.model

@Deprecated("Should not be used anymore")
data class WinnerSeason(
    val season: Int,
    val driver: List<WinnerSeasonDriver>,
    val constructor: List<WinnerSeasonConstructor>
)

@Deprecated("Should not be used anymore")
data class WinnerSeasonDriver(
    val id: String,
    val name: String,
    val image: String?,
    val points: Int
)

@Deprecated("Should not be used anymore")
data class WinnerSeasonConstructor(
    val id: String,
    val name: String,
    val color: String,
    val points: Int
)
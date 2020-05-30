package tmg.flashback.repo.models

data class WinnerSeason(
    val season: Int,
    val driver: List<WinnerSeasonDriver>,
    val constructor: List<WinnerSeasonConstructor>
)

data class WinnerSeasonDriver(
    val id: String,
    val name: String,
    val img: String,
    val p: Int
)

data class WinnerSeasonConstructor(
    val id: String,
    val name: String,
    val color: String,
    val p: Int
)
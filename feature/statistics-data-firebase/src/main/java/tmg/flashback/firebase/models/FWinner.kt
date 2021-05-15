package tmg.flashback.firebase.models

data class FWinner(
    val win: Map<String, FWinnerSeason>? = emptyMap()
)

data class FWinnerSeason(
    val s: Int? = 0,
    val driver: List<FWinnerSeasonDriver>? = emptyList(),
    val constr: List<FWinnerSeasonConstructor>? = emptyList()
)

data class FWinnerSeasonDriver(
    val id: String = "",
    val name: String = "",
    val img: String? = null,
    val p: Int = 0
)

data class FWinnerSeasonConstructor(
    val id: String = "",
    val name: String = "",
    val color: String? = null,
    val p: Int = 0
)
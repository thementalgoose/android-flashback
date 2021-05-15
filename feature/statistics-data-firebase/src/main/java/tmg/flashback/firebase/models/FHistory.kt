package tmg.flashback.firebase.models

data class FHistorySeason(
    val all: Map<String, Map<String, FHistorySeasonRound?>?> = mapOf(),
    val win: Map<String, FHistorySeasonWin>? = null
)

data class FHistorySeasonRound(
    val date: String = "",
    val r: Int = -1, // Round
    val s: Int = -1, // Season
    val circuitId: String = "",
    val country: String = "",
    val countryISO: String = "",
    val circuit: String = "",
    val name: String = "",
    val data: Boolean? = null,
    val hasQ: Boolean? = null,
    val hasR: Boolean? = null
)

data class FHistorySeasonWin(
    val s: Int = 0, // Season
    val constr: List<FHistorySeasonWinConstructor>? = emptyList(),
    val driver: List<FHistorySeasonWinDriver>? = emptyList()
)

data class FHistorySeasonWinConstructor(
    val id: String = "",
    val name: String = "",
    val color: String = "",
    val p: Int = 0 // Points
)

data class FHistorySeasonWinDriver(
    val id: String = "",
    val name: String = "",
    val img: String = "",
    val p: Int = 0 // Points
)
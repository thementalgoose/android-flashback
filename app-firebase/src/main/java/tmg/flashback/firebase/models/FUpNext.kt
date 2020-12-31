package tmg.flashback.firebase.models

data class FUpNext(
    val schedule: List<FUpNextSchedule>? = null
)

data class FUpNextSchedule(
    val s: Int? = null,
    val r: Int? = null,
    val name: String? = null,
    val date: String? = null,
    val time: String? = null,
    val flag: String? = null,
    val circuit: String? = null,
    val circuitName: String? = null
)
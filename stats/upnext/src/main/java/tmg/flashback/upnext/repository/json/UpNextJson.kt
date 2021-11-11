package tmg.flashback.upnext.repository.json

data class UpNextJson(
        val schedule: List<UpNextScheduleJson>? = null
)

data class UpNextScheduleJson(
        val s: Int? = null,
        val r: Int? = null,
        val title: String? = null,
        val subtitle: String? = null,
        val dates: List<UpNextItemJson>? = null,
        val flag: String? = null,
        val circuit: String? = null,
)

data class UpNextItemJson(
        val type: String?,
        val d: String?,
        val t: String?
)
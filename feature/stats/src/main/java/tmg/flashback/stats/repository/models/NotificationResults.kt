package tmg.flashback.stats.repository.models

data class NotificationResults(
    val qualifying: Boolean,
    val sprint: Boolean,
    val race: Boolean
)
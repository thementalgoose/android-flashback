package tmg.flashback.results.repository.models

data class NotificationResults(
    val qualifying: Boolean,
    val sprint: Boolean,
    val race: Boolean
)
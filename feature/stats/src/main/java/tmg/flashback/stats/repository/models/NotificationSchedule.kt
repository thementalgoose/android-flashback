package tmg.flashback.stats.repository.models

data class NotificationSchedule(
    val freePractice: Boolean,
    val qualifying: Boolean,
    val race: Boolean,
    val other: Boolean
)
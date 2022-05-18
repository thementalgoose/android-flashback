package tmg.flashback.stats.repository.models

import tmg.flashback.formula1.enums.RaceWeekend
import tmg.flashback.formula1.utils.NotificationUtils

data class NotificationSchedule(
    val freePractice: Boolean,
    val qualifying: Boolean,
    val race: Boolean,
    val other: Boolean
) {
    fun getByLabel(label: String): Boolean {
        return when (NotificationUtils.getCategoryBasedOnLabel(label)) {
            RaceWeekend.FREE_PRACTICE -> freePractice
            RaceWeekend.QUALIFYING -> qualifying
            RaceWeekend.RACE -> race
            null -> other
        }
    }
}
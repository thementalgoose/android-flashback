package tmg.flashback.formula1.utils

import tmg.flashback.formula1.enums.RaceWeekend

object NotificationUtils {

    /**
     * Get which category the notification should be grouped in to based on the label of the event
     */
    fun getCategoryBasedOnLabel(label: String): RaceWeekend? {
        return when {
            label.includes(
                "shootout",
                "sprint shootout"
            ) -> RaceWeekend.SPRINT_QUALIFYING
            label.includes(
                "sprint",
                "sprint qualifying",
                "sprint quali",
            ) -> RaceWeekend.SPRINT
            label.includes(
                "qualifying",
                "quali"
            ) -> RaceWeekend.QUALIFYING
            label.includes(
                "race",
                "grand prix",
            ) -> RaceWeekend.RACE
            label.includes(
                "fp",
                "free practice",
                "practice"
            ) -> RaceWeekend.FREE_PRACTICE
            else -> null
        }
    }

    private fun String.includes(vararg partials: String): Boolean {
        return partials.any { label ->
            this.lowercase().contains(label.lowercase())
        }
    }
}
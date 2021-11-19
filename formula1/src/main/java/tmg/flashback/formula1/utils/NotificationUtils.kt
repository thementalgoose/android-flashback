package tmg.flashback.formula1.utils

import tmg.flashback.formula1.enums.RaceWeekend

object NotificationUtils {

    /**
     * Get which category the notification should be grouped in to based on the label of the event
     */
    fun getCategoryBasedOnLabel(label: String): RaceWeekend? {
        return when {
            label.includes(
                "race",
                "grand prix"
            ) -> RaceWeekend.RACE
            label.includes(
                "qualifying",
                "sprint qualifying",
                "sprint quali",
                "sprint",
                "quali"
            ) -> RaceWeekend.QUALIFYING
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
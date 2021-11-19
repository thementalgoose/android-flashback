package tmg.flashback.statistics.controllers

interface UpNextControllerDelegate {
    val notificationsRaceEnabled: Boolean
    val notificationsQualifyingEnabled: Boolean
    val notificationsFreePracticeEnabled: Boolean

}
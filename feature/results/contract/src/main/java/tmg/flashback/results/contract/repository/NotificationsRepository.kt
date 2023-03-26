package tmg.flashback.results.contract.repository

interface NotificationsRepository {
    var notificationUpcomingRace: Boolean
    var notificationUpcomingSprint: Boolean
    var notificationUpcomingQualifying: Boolean
    var notificationUpcomingFreePractice: Boolean
    var notificationUpcomingOther: Boolean
}
package tmg.flashback.repo.pref

import tmg.flashback.repo.enums.NotificationRegistration

interface PrefNotificationDB {

    /**
     * Race Notification preference
     *   null = hasn't been set
     */
    var notificationsRace: NotificationRegistration?

    /**
     * Qualifying Notification preference
     *   null = hasn't been set
     */
    var notificationsQualifying: NotificationRegistration?

    /**
     * Miscellaneous Notification preference
     *   null = hasn't been set
     */
    var notificationsMisc: NotificationRegistration?
}
package tmg.flashback.upnext.repository

import tmg.configuration.manager.ConfigManager
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.upnext.model.NotificationReminder
import tmg.flashback.upnext.repository.converters.convert
import tmg.flashback.upnext.repository.json.UpNextJson
import tmg.flashback.upnext.repository.model.UpNextSchedule
import tmg.utilities.extensions.toEnum

class UpNextRepository(
    private val configManager: ConfigManager,
    private val preferenceManager: PreferenceManager
) {

    companion object {
        private const val keyUpNext: String = "up_next"

        private const val keyNotificationRace: String = "UP_NEXT_NOTIFICATION_RACE"
        private const val keyNotificationQualifying: String = "UP_NEXT_NOTIFICATION_QUALIFYING"
        private const val keyNotificationFreePractice: String = "UP_NEXT_NOTIFICATION_FREE_PRACTICE"
        private const val keyNotificationOther: String = "UP_NEXT_NOTIFICATION_OTHER"
        private const val keyNotificationReminder: String = "UP_NEXT_NOTIFICATION_REMINDER"

        private const val keyNotificationOnboarding: String = "UP_NEXT_NOTIFICATION_ONBOARDING"
    }

    val upNext: List<UpNextSchedule>
        get() = configManager
                .getJson<UpNextJson>(keyUpNext)
                ?.convert()
                ?: emptyList()

    var notificationRace: Boolean
        get() = preferenceManager.getBoolean(keyNotificationRace, true)
        set(value) = preferenceManager.save(keyNotificationRace, value)

    var notificationQualifying: Boolean
        get() = preferenceManager.getBoolean(keyNotificationQualifying, true)
        set(value) = preferenceManager.save(keyNotificationQualifying, value)

    var notificationFreePractice: Boolean
        get() = preferenceManager.getBoolean(keyNotificationFreePractice, false)
        set(value) = preferenceManager.save(keyNotificationFreePractice, value)

    var notificationOther: Boolean
        get() = preferenceManager.getBoolean(keyNotificationOther, false)
        set(value) = preferenceManager.save(keyNotificationOther, value)

    var seenNotificationOnboarding: Boolean
        get() = preferenceManager.getBoolean(keyNotificationOnboarding, false)
        set(value) = preferenceManager.save(keyNotificationOnboarding, value)

    var notificationReminderPeriod: NotificationReminder
        get() = preferenceManager.getInt(keyNotificationReminder, NotificationReminder.MINUTES_30.seconds).toEnum<NotificationReminder> { it.seconds } ?: NotificationReminder.MINUTES_30
        set(value) = preferenceManager.save(keyNotificationReminder, value.seconds)
}
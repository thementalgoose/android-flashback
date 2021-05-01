package tmg.notifications.repository

import tmg.flashback.device.repository.SharedPreferenceRepository
import tmg.notifications.NotificationRegistration
import tmg.utilities.extensions.toEnum

class NotificationRepository(
    private val sharedPreferenceRepository: SharedPreferenceRepository
) {

    companion object {
        private const val keyNotificationRace: String = "NOTIFICATION_RACE"
        private const val keyNotificationQualifying: String = "NOTIFICATION_QUALIFYING"
        private const val keyNotificationSeasonInfo: String = "NOTIFICATION_SEASON_INFO"
    }

    var enabledRace: NotificationRegistration
        set(value) = sharedPreferenceRepository.save(keyNotificationRace, value.key)
        get() = sharedPreferenceRepository
            .getString(keyNotificationRace, "")
            ?.toEnum<NotificationRegistration> {
                it.key
            }
            ?: NotificationRegistration.DEFAULT

    var enabledQualifying: NotificationRegistration
        set(value) = sharedPreferenceRepository.save(keyNotificationQualifying, value.key)
        get() = sharedPreferenceRepository
            .getString(keyNotificationQualifying, "")
            ?.toEnum<NotificationRegistration> {
                it.key
            }
            ?: NotificationRegistration.DEFAULT

    var enabledSeasonInfo: NotificationRegistration
        set(value) = sharedPreferenceRepository.save(keyNotificationSeasonInfo, value.key)
        get() = sharedPreferenceRepository
            .getString(keyNotificationSeasonInfo, "")
            ?.toEnum<NotificationRegistration> {
                it.key
            }
            ?: NotificationRegistration.DEFAULT

}
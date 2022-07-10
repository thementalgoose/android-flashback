package tmg.flashback.statistics.controllers

import android.content.Context
import android.util.Log
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.notifications.usecases.RemoteNotificationSubscribeUseCase
import tmg.flashback.notifications.usecases.RemoteNotificationUnsubscribeUseCase
import tmg.flashback.statistics.BuildConfig
import tmg.flashback.statistics.repo.ScheduleRepository
import tmg.flashback.statistics.repository.UpNextRepository
import tmg.flashback.statistics.repository.models.NotificationReminder

/**
 * Information around scheduling notificatoins functionality on the home screen
 */
@Deprecated("This class will be replaced with use cases")
class ScheduleController(
    private val applicationContext: Context,
    private val remoteNotificationSubscribeUseCase: RemoteNotificationSubscribeUseCase,
    private val remoteNotificationUnsubscribeUseCase: RemoteNotificationUnsubscribeUseCase,
    private val upNextRepository: UpNextRepository,
    private val scheduleRepository: ScheduleRepository,
) {
    /**
     * Get the next race to display in the up next schedule
     *  Up to and including today
     */
    // TODO: Move this to a different controller
    suspend fun getNextEvent(): OverviewRace? {
        return scheduleRepository
            .getUpcomingEvents()
            .minByOrNull { it.date }
    }

    //region Notification preferences

    /**
     * Should the user be prompted for showing a notification onboarding bottom sheet
     */
    val shouldShowNotificationOnboarding: Boolean
        get() = !upNextRepository.seenNotificationOnboarding

    /**
     * Tell the system that we've seen the onboarding sheet
     */
    fun seenOnboarding() {
        upNextRepository.seenNotificationOnboarding = true
    }

    var notificationRace: Boolean
        get() = upNextRepository.notificationRace
        set(value) {
            upNextRepository.notificationRace = value
            scheduleNotifications()
        }

    var notificationQualifying: Boolean
        get() = upNextRepository.notificationQualifying
        set(value) {
            upNextRepository.notificationQualifying = value
            scheduleNotifications()
        }

    var notificationFreePractice: Boolean
        get() = upNextRepository.notificationFreePractice
        set(value) {
            upNextRepository.notificationFreePractice = value
            scheduleNotifications()
        }

    var notificationSeasonInfo: Boolean
        get() = upNextRepository.notificationOther
        set(value) {
            upNextRepository.notificationOther = value
            scheduleNotifications()
        }

    var notificationReminder: NotificationReminder
        get() = upNextRepository.notificationReminderPeriod
        set(value) {
            upNextRepository.notificationReminderPeriod = value
            scheduleNotifications()
        }

    suspend fun resubscribe() {
        when (notificationRaceNotify) {
            true -> remoteNotificationSubscribeUseCase.subscribe("notify_race")
            false -> remoteNotificationUnsubscribeUseCase.unsubscribe("notify_race")
        }
        when (notificationSprintNotify) {
            true -> remoteNotificationSubscribeUseCase.subscribe("notify_sprint")
            false -> remoteNotificationUnsubscribeUseCase.unsubscribe("notify_sprint")
        }
        when (notificationQualifyingNotify) {
            true -> remoteNotificationSubscribeUseCase.subscribe("notify_qualifying")
            false -> remoteNotificationUnsubscribeUseCase.unsubscribe("notify_qualifying")
        }
    }

    var notificationRaceNotify: Boolean
        get() = upNextRepository.notificationNotifyRace
        set(value) {
            upNextRepository.notificationNotifyRace = value
        }

    var notificationSprintNotify: Boolean
        get() = upNextRepository.notificationNotifySprint
        set(value) {
            upNextRepository.notificationNotifySprint = value
        }

    var notificationQualifyingNotify: Boolean
        get() = upNextRepository.notificationNotifyQualifying
        set(value) {
            upNextRepository.notificationNotifyQualifying = value
        }



    fun scheduleNotifications() {
        if (BuildConfig.DEBUG) {
            Log.i("Notifications", "WorkManager performing notification scheduling")
        }
    }
}
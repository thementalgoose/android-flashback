package tmg.flashback.statistics.controllers

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.ExistingWorkPolicy.REPLACE
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import tmg.flashback.formula1.enums.RaceWeekend
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.Timestamp
import tmg.flashback.formula1.utils.NotificationUtils.getCategoryBasedOnLabel
import tmg.flashback.notifications.controllers.NotificationController
import tmg.flashback.statistics.BuildConfig
import tmg.flashback.statistics.repo.ScheduleRepository
import tmg.flashback.statistics.repository.UpNextRepository
import tmg.flashback.statistics.repository.models.NotificationReminder
import tmg.flashback.statistics.utils.NotificationUtils.getNotificationTitleText
import tmg.flashback.statistics.workmanager.NotificationScheduler
import java.util.*

/**
 * Information around scheduling notificatoins functionality on the home screen
 */
class ScheduleController(
    private val applicationContext: Context,
    private val notificationController: NotificationController,
    private val upNextRepository: UpNextRepository,
    private val scheduleRepository: ScheduleRepository
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



    var notificationRaceNotify: Boolean
        get() = upNextRepository.notificationNotifyRace
        set(value) {
            upNextRepository.notificationNotifyRace = value
            GlobalScope.launch {
                when (value) {
                    true -> notificationController.subscribeToRemoteNotification("notify_race")
                    false -> notificationController.unsubscribeToRemoteNotification("notify_race")
                }
            }
        }

    var notificationQualifyingNotify: Boolean
        get() = upNextRepository.notificationNotifyQualifying
        set(value) {
            upNextRepository.notificationNotifyQualifying = value
            GlobalScope.launch {
                when (value) {
                    true -> notificationController.subscribeToRemoteNotification("notify_qualifying")
                    false -> notificationController.unsubscribeToRemoteNotification("notify_qualifying")
                }
            }
        }



    fun scheduleNotifications() {
        if (BuildConfig.DEBUG) {
            Log.i("Notifications", "WorkManager performing notification scheduling")
        }
        NotificationScheduler.schedule(applicationContext)
    }
}
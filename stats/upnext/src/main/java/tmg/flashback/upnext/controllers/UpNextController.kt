package tmg.flashback.upnext.controllers

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.Timestamp
import tmg.flashback.notifications.controllers.NotificationController
import tmg.flashback.statistics.repo.ScheduleRepository
import tmg.flashback.upnext.BuildConfig
import tmg.flashback.upnext.model.NotificationChannel
import tmg.flashback.upnext.model.NotificationReminder
import tmg.flashback.upnext.repository.UpNextRepository
import tmg.flashback.upnext.repository.model.UpNextSchedule
import tmg.flashback.upnext.utils.NotificationUtils
import tmg.flashback.upnext.utils.NotificationUtils.getCategoryBasedOnLabel
import tmg.flashback.upnext.utils.NotificationUtils.getNotificationTitleText

/**
 * Up Next functionality on the home screen
 */
class UpNextController(
    private val applicationContext: Context,
    private val notificationController: NotificationController,
    private val upNextRepository: UpNextRepository,
    private val scheduleRepository: ScheduleRepository
) {

    /**
     * Get the next race to display in the up next schedule
     *  Up to and including today
     */
    // item 0 :yesterday
    // item 1 :today, today2
    // item 2: tomorrow
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
            GlobalScope.launch(context = Dispatchers.IO) { scheduleNotifications(force = true) }
        }

    var notificationQualifying: Boolean
        get() = upNextRepository.notificationQualifying
        set(value) {
            upNextRepository.notificationQualifying = value
            GlobalScope.launch(context = Dispatchers.IO) { scheduleNotifications(force = true) }
        }

    var notificationFreePractice: Boolean
        get() = upNextRepository.notificationFreePractice
        set(value) {
            upNextRepository.notificationFreePractice = value
            GlobalScope.launch(context = Dispatchers.IO) { scheduleNotifications(force = true) }
        }

    var notificationSeasonInfo: Boolean
        get() = upNextRepository.notificationOther
        set(value) {
            upNextRepository.notificationOther = value
            GlobalScope.launch(context = Dispatchers.IO) { scheduleNotifications(force = true) }
        }

    var notificationReminder: NotificationReminder
        get() = upNextRepository.notificationReminderPeriod
        set(value) {
            upNextRepository.notificationReminderPeriod = value
            GlobalScope.launch(context = Dispatchers.IO) { scheduleNotifications(force = true) }
        }

    /**
     * Schedule notifications
     */
    suspend fun scheduleNotifications(force: Boolean = false) {
        val upNextItemsToSchedule = scheduleRepository
            .getUpcomingEvents()
            .map { event ->
                event.schedule.mapIndexed { index, item ->
                    NotificationModel(
                        season = event.season,
                        round = event.round,
                        value = index,
                        title = event.raceName,
                        label = item.label,
                        timestamp = item.timestamp,
                        channel = getCategoryBasedOnLabel(item.label)
                    )
                }
            }
            .flatten()
            .filter { it.timestamp.originalTime != null }
            .filter { !it.timestamp.isInPastRelativeToo(upNextRepository.notificationReminderPeriod.seconds.toLong()) }
            .map {
                it.apply {
                    var utcDateTime: LocalDateTime = it.timestamp.originalDate.atTime(it.timestamp.originalTime)
                    it.timestamp.on(
                        dateAndTime = { utc, _ ->
                            utcDateTime = utc
                        }
                    )

                    this.utcDateTime = utcDateTime
                    this.requestCode = NotificationUtils.getRequestCode(utcDateTime)
                }
            }
            .filter {
                when (it.channel) {
                    NotificationChannel.RACE -> notificationRace
                    NotificationChannel.QUALIFYING -> notificationQualifying
                    NotificationChannel.FREE_PRACTICE -> notificationFreePractice
                    NotificationChannel.SEASON_INFO -> notificationSeasonInfo
                }
            }

        if (upNextItemsToSchedule.map { it.requestCode }.toSet() == notificationController.notificationsCurrentlyScheduled && !force) {
            if (BuildConfig.DEBUG) {
                Log.d("Flashback", "Up Next items have remained unchanged since last sync - Skipping scheduling")
            }
            return
        }

        notificationController.cancelAllNotifications()

        val reminderPeriod = upNextRepository.notificationReminderPeriod

        upNextItemsToSchedule.forEach {

            // Remove the notification reminder period
            val scheduleTime = it.utcDateTime.minusSeconds(reminderPeriod.seconds.toLong())

            val (title, text) = getNotificationTitleText(applicationContext, it.title, it.label, it.timestamp, reminderPeriod)

            notificationController.scheduleLocalNotification(
                requestCode = it.requestCode,
                channelId = getCategoryBasedOnLabel(it.label).channelId,
                title = title,
                text = text,
                timestamp = scheduleTime
            )
        }
    }

    //endregion

    inner class NotificationModel(
        val season: Int,
        val round: Int,
        val value: Int,
        val title: String,
        val label: String,
        val timestamp: Timestamp,
        val channel: NotificationChannel
    ) {
        var requestCode: Int = -1
        lateinit var utcDateTime: LocalDateTime
    }
}
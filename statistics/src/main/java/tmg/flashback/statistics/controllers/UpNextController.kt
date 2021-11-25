package tmg.flashback.statistics.controllers

import android.content.Context
import android.util.Log
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
import tmg.flashback.statistics.utils.NotificationUtils.getNotificationTitleText

private typealias UpNextNotificationUtils = tmg.flashback.statistics.utils.NotificationUtils

/**
 * Up Next functionality on the home screen
 */
class UpNextController(
    private val applicationContext: Context,
    private val notificationController: NotificationController,
    private val upNextRepository: tmg.flashback.statistics.repository.UpNextRepository,
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

    var notificationReminder: tmg.flashback.statistics.repository.models.NotificationReminder
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
                        channel = item.label.toChannel()
                    )
                }
            }
            .flatten()
            .filter { !it.timestamp.isInPastRelativeToo(upNextRepository.notificationReminderPeriod.seconds.toLong()) }
            .map {
                it.apply {
                    this.utcDateTime = it.timestamp.utcLocalDateTime
                    this.requestCode = tmg.flashback.statistics.utils.NotificationUtils.getRequestCode(utcDateTime)
                }
            }
            .filter {
                when (it.channel) {
                    tmg.flashback.statistics.repository.models.NotificationChannel.RACE -> notificationRace
                    tmg.flashback.statistics.repository.models.NotificationChannel.QUALIFYING -> notificationQualifying
                    tmg.flashback.statistics.repository.models.NotificationChannel.FREE_PRACTICE -> notificationFreePractice
                    tmg.flashback.statistics.repository.models.NotificationChannel.SEASON_INFO -> notificationSeasonInfo
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
                channelId = it.label.toChannel().channelId,
                title = title,
                text = text,
                timestamp = scheduleTime
            )
        }
    }

    private fun String.toChannel(): tmg.flashback.statistics.repository.models.NotificationChannel {
        return when (getCategoryBasedOnLabel(this)) {
            RaceWeekend.FREE_PRACTICE -> tmg.flashback.statistics.repository.models.NotificationChannel.FREE_PRACTICE
            RaceWeekend.QUALIFYING -> tmg.flashback.statistics.repository.models.NotificationChannel.QUALIFYING
            RaceWeekend.RACE -> tmg.flashback.statistics.repository.models.NotificationChannel.RACE
            null -> tmg.flashback.statistics.repository.models.NotificationChannel.SEASON_INFO
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
        val channel: tmg.flashback.statistics.repository.models.NotificationChannel
    ) {
        var requestCode: Int = -1
        lateinit var utcDateTime: LocalDateTime
    }
}
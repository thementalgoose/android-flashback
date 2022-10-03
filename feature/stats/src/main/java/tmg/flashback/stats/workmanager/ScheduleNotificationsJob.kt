package tmg.flashback.stats.workmanager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDateTime
import tmg.flashback.formula1.enums.RaceWeekend
import tmg.flashback.formula1.model.Timestamp
import tmg.flashback.formula1.utils.NotificationUtils
import tmg.flashback.notifications.repository.NotificationRepository
import tmg.flashback.notifications.usecases.LocalNotificationCancelUseCase
import tmg.flashback.notifications.usecases.LocalNotificationScheduleUseCase
import tmg.flashback.statistics.repo.ScheduleRepository
import tmg.flashback.stats.BuildConfig
import tmg.flashback.stats.repository.models.NotificationChannel

@HiltWorker
class ScheduleNotificationsJob @AssistedInject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val notificationConfigRepository: NotificationRepository,
    private val localNotificationCancelUseCase: LocalNotificationCancelUseCase,
    private val localNotificationScheduleUseCase: LocalNotificationScheduleUseCase,
    private val notificationRepository: tmg.flashback.stats.repository.NotificationRepository,
    @Assisted context: Context,
    @Assisted parameters: WorkerParameters
): CoroutineWorker(
    context,
    parameters
) {
    override suspend fun doWork(): ListenableWorker.Result = withContext(Dispatchers.IO) {
        if (isStopped) {
            return@withContext Result.success()
        }

        val force: Boolean = inputData.getBoolean("force", false)

        if (BuildConfig.DEBUG) {
            Log.i("Notification", "WorkManager - Scheduling notifications")
        }

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
            .filter { !it.timestamp.isInPastRelativeToo(notificationRepository.notificationReminderPeriod.seconds.toLong()) }
            .map {
                it.apply {
                    this.utcDateTime = it.timestamp.utcLocalDateTime
                    this.requestCode = tmg.flashback.stats.utils.NotificationUtils.getRequestCode(utcDateTime)
                }
            }
            .filter {
                when (it.channel) {
                    NotificationChannel.RACE -> notificationRepository.notificationUpcomingRace
                    NotificationChannel.SPRINT -> notificationRepository.notificationUpcomingSprint
                    NotificationChannel.QUALIFYING -> notificationRepository.notificationUpcomingQualifying
                    NotificationChannel.FREE_PRACTICE -> notificationRepository.notificationUpcomingFreePractice
                    NotificationChannel.SEASON_INFO -> notificationRepository.notificationUpcomingOther
                }
            }


        if (BuildConfig.DEBUG) {
            Log.i("Notification", "WorkManager notificationsScheduled ${notificationConfigRepository.notificationIds}")
            Log.i("Notification", "WorkManager upNextItems to schedule ${upNextItemsToSchedule.size}")
            Log.i("Notification", "WorkManager upNextItems to schedule $upNextItemsToSchedule")
        }
        if (upNextItemsToSchedule.map { it.requestCode }.toSet() == notificationConfigRepository.notificationIds && !force) {
            if (BuildConfig.DEBUG) {
                Log.d("Notification", "WorkManager - Up Next items have remained unchanged since last sync - Skipping scheduling of notifications")
            }
            return@withContext Result.success()
        }

        localNotificationCancelUseCase.cancelAll()

        val reminderPeriod = notificationRepository.notificationReminderPeriod

        upNextItemsToSchedule.forEach {

            // Remove the notification reminder period
            val scheduleTime = it.utcDateTime.minusSeconds(reminderPeriod.seconds.toLong())

            val (title, text) = tmg.flashback.stats.utils.NotificationUtils.getNotificationTitleText(
                applicationContext,
                it.title,
                it.label,
                it.timestamp,
                reminderPeriod
            )

            localNotificationScheduleUseCase.schedule(
                requestCode = it.requestCode,
                channelId = it.label.toChannel().channelId,
                title = title,
                text = text,
                timestamp = scheduleTime
            )
            if (BuildConfig.DEBUG) {
                Log.i("Notification", "WorkManager - Notification at $scheduleTime - $title scheduled")
            }
        }

        if (BuildConfig.DEBUG) {
            Log.i("Notification", "WorkManager - Finished scheduling notifications")
        }

        return@withContext Result.success()
    }

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

    private fun String.toChannel(): NotificationChannel {
        return when (NotificationUtils.getCategoryBasedOnLabel(this)) {
            RaceWeekend.FREE_PRACTICE -> NotificationChannel.FREE_PRACTICE
            RaceWeekend.QUALIFYING -> NotificationChannel.QUALIFYING
            RaceWeekend.SPRINT -> NotificationChannel.SPRINT
            RaceWeekend.RACE -> NotificationChannel.RACE
            null -> NotificationChannel.SEASON_INFO
        }
    }
}
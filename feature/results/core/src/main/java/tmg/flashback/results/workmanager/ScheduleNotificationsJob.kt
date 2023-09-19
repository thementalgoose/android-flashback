package tmg.flashback.results.workmanager

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
import tmg.flashback.device.repository.PermissionRepository
import tmg.flashback.domain.repo.ScheduleRepository
import tmg.flashback.formula1.enums.RaceWeekend
import tmg.flashback.formula1.model.Timestamp
import tmg.flashback.formula1.utils.NotificationUtils
import tmg.flashback.notifications.repository.NotificationRepository
import tmg.flashback.notifications.usecases.LocalNotificationCancelUseCase
import tmg.flashback.notifications.usecases.LocalNotificationScheduleUseCase
import tmg.flashback.results.contract.repository.NotificationsRepository
import tmg.flashback.results.contract.repository.models.NotificationUpcoming
import tmg.flashback.results.contract.repository.models.NotificationUpcoming.FREE_PRACTICE
import tmg.flashback.results.contract.repository.models.NotificationUpcoming.OTHER
import tmg.flashback.results.contract.repository.models.NotificationUpcoming.QUALIFYING
import tmg.flashback.results.contract.repository.models.NotificationUpcoming.RACE
import tmg.flashback.results.contract.repository.models.NotificationUpcoming.SPRINT
import tmg.flashback.results.contract.repository.models.NotificationUpcoming.SPRINT_QUALIFYING
import tmg.flashback.results.contract.utils.NotificationUtils.getInexactNotificationTitleText
import tmg.flashback.results.contract.utils.NotificationUtils.getNotificationTitleText

@HiltWorker
class ScheduleNotificationsJob @AssistedInject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val notificationConfigRepository: NotificationRepository,
    private val localNotificationCancelUseCase: LocalNotificationCancelUseCase,
    private val localNotificationScheduleUseCase: LocalNotificationScheduleUseCase,
    private val notificationRepository: NotificationsRepository,
    private val permissionRepository: PermissionRepository,
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

        Log.i("Notification", "WorkManager - Scheduling notifications")

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
                    this.requestCode = tmg.flashback.results.contract.utils.NotificationUtils.getRequestCode(utcDateTime)
                }
            }


        Log.i("Notification", "WorkManager notificationsScheduled ${notificationConfigRepository.notificationIds}")
        Log.i("Notification", "WorkManager upNextItems to schedule ${upNextItemsToSchedule.size}")
        Log.i("Notification", "WorkManager upNextItems to schedule $upNextItemsToSchedule")
        if (upNextItemsToSchedule.map { it.requestCode }.toSet() == notificationConfigRepository.notificationIds && !force) {
            Log.d("Notification", "WorkManager - Up Next items have remained unchanged since last sync - Skipping scheduling of notifications")
            return@withContext Result.success()
        }

        localNotificationCancelUseCase.cancelAll()

        val reminderPeriod = notificationRepository.notificationReminderPeriod

        upNextItemsToSchedule.forEach {

            val exactSupported = permissionRepository.isExactAlarmEnabled

            // Remove the notification reminder period
            when (exactSupported) {
                true -> {
                    val time = it.utcDateTime.minusSeconds(reminderPeriod.seconds.toLong())
                    val (title, text) = getNotificationTitleText(
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
                        timestamp = time,
                        exact = true,
                    )

                    Log.i("Notification", "WorkManager - Exact Notification at $time - $title scheduled")
                }
                false -> {
                    val time = it.utcDateTime.minusHours(1L)
                    val (title, text) = getInexactNotificationTitleText(
                        applicationContext,
                        it.title,
                        it.label,
                        it.timestamp
                    )
                    localNotificationScheduleUseCase.schedule(
                        requestCode = it.requestCode,
                        channelId = it.label.toChannel().channelId,
                        title = title,
                        text = text,
                        timestamp = time,
                        exact = false,
                    )

                    Log.i("Notification", "WorkManager - Inexact Notification at $time - $title scheduled")
                }
            }
        }

        Log.i("Notification", "WorkManager - Finished scheduling notifications")

        return@withContext Result.success()
    }

    inner class NotificationModel(
        val season: Int,
        val round: Int,
        val value: Int,
        val title: String,
        val label: String,
        val timestamp: Timestamp,
        val channel: NotificationUpcoming
    ) {
        var requestCode: Int = -1
        lateinit var utcDateTime: LocalDateTime
    }

    private fun String.toChannel(): NotificationUpcoming {
        return when (NotificationUtils.getCategoryBasedOnLabel(this)) {
            RaceWeekend.FREE_PRACTICE -> FREE_PRACTICE
            RaceWeekend.QUALIFYING -> QUALIFYING
            RaceWeekend.SPRINT_QUALIFYING -> SPRINT_QUALIFYING
            RaceWeekend.SPRINT -> SPRINT
            RaceWeekend.RACE -> RACE
            null -> OTHER
        }
    }
}
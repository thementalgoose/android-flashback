package tmg.flashback.statistics.workmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.Clock
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.notifications.controllers.NotificationController
import tmg.flashback.statistics.repo.ScheduleRepository
import tmg.flashback.statistics.repository.UpNextRepository
import tmg.flashback.statistics.repository.models.NotificationReminder
import tmg.flashback.statistics.utils.NotificationUtils
import tmg.testutils.BaseTest

internal class NotificationScheduleWorkerTest: BaseTest() {

    private val mockScheduleRepository: ScheduleRepository = mockk(relaxed = true)
    private val mockNotificationController: NotificationController = mockk(relaxed = true)
    private val mockUpNextRepository: UpNextRepository = mockk(relaxed = true)
    private val mockContext: Context = mockk(relaxed = true)
    private val mockParameters: WorkerParameters = mockk(relaxed = true)

    private lateinit var sut: NotificationScheduleWorker

    private fun initSUT() {
        sut = NotificationScheduleWorker(
            mockScheduleRepository,
            mockNotificationController,
            mockUpNextRepository,
            mockContext,
            mockParameters
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockUpNextRepository.notificationReminderPeriod } returns NotificationReminder.MINUTES_30

        every { mockUpNextRepository.notificationRace } returns true
        every { mockUpNextRepository.notificationQualifying } returns true
        every { mockUpNextRepository.notificationFreePractice } returns true
        every { mockUpNextRepository.notificationOther } returns true
    }

    //region Schedule notification filtering

    @Test
    fun `when finding notifications to schedule it scheduled accurately the local notifications with the manager`() = coroutineTest {

        coEvery { mockScheduleRepository.getUpcomingEvents(any()) } returns exampleUpNextList
        coEvery { mockNotificationController.notificationsCurrentlyScheduled } returns emptySet()

        initSUT()
        runBlockingTest {
            val result = sut.doWork()
            assertTrue(result is ListenableWorker.Result.Success)
        }

        verify {
            mockNotificationController.cancelAllNotifications()
        }

        verifyScheduleLocal(times = 0, past, 0, past.schedule[0], "flashback_qualifying")
        verifyScheduleLocal(times = 0, past, 1, past.schedule[1], "flashback_race")
        verifyScheduleLocal(times = 0, present, 0, present.schedule[0], "flashback_free_practice")

        // Qualifying while in the future is ahead of 30 minute notice period!
        verifyScheduleLocal(times = 0, present, 1, present.schedule[1], "flashback_qualifying")

        verifyScheduleLocal(times = 1, present, 2, present.schedule[2], "flashback_race")
        verifyScheduleLocal(times = 1, future, 0, future.schedule[0], "flashback_free_practice")
    }

    //endregion

    private fun verifyScheduleLocal(times: Int = 1, upNextSchedule: OverviewRace, index: Int, item: Schedule, channelId: String) {
        var timestampUtc: LocalDateTime = item.timestamp.utcLocalDateTime
        val requestCode = NotificationUtils.getRequestCode(timestampUtc)
        timestampUtc = timestampUtc.minusMinutes(30)

        verify(exactly = times) {
            mockNotificationController.scheduleLocalNotification(
                requestCode = requestCode,
                channelId = channelId,
                title = "" /* TODO: Inject the NotificationUtils properly - This is handled there! */,
                text = "" /* TODO: Inject the NotificationUtils properly - This is handled there! */,
                timestamp = timestampUtc
            )
        }
    }

    private val past: OverviewRace = generateUpNextItem(
        season = 2020,
        round = 2,
        "qualifying" to LocalDateTime.now(Clock.systemUTC()).minusDays(2L),
        "race" to LocalDateTime.now(Clock.systemUTC()).minusDays(1L)
    )
    private val present: OverviewRace = generateUpNextItem(
        season = 2020,
        round = 3,
        "fp3" to LocalDateTime.now(Clock.systemUTC()).minusHours(1L),
        "qualifying" to LocalDateTime.now(Clock.systemUTC()).plusMinutes(5L),
        "race" to LocalDateTime.now(Clock.systemUTC()).plusHours(8L)
    )
    private val future: OverviewRace = generateUpNextItem(
        season = 2020,
        round = 4,
        "fp3" to LocalDateTime.now(Clock.systemUTC()).plusDays(1L)
    )
    private val exampleUpNextList: List<OverviewRace> = listOf(
        past, present, future
    )

    private fun generateUpNextItem(
        season: Int,
        round: Int,
        vararg delta: Pair<String, LocalDateTime>
    ): OverviewRace {
        return OverviewRace(
            date = LocalDate.now(),
            time = LocalTime.of(0, 12, 0),
            season = season,
            round = round,
            raceName = "Grand Prix $season $round",
            circuitId = "circuitId",
            circuitName = "circuitName",
            country = "country",
            countryISO = "countryISO",
            hasQualifying = true,
            hasResults = true,
            schedule = delta.map { (label, datetime) ->
                Schedule(
                    label = label,
                    date = datetime.toLocalDate(),
                    time = datetime.toLocalTime()
                )
            }
        )
    }

}
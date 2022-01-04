package tmg.flashback.statistics.controllers

import android.content.Context
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.Clock
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.statistics.utils.NotificationUtils.getRequestCode
import tmg.flashback.notifications.controllers.NotificationController
import tmg.flashback.statistics.repo.ScheduleRepository
import tmg.flashback.statistics.repository.UpNextRepository
import tmg.flashback.statistics.repository.models.NotificationReminder
import tmg.flashback.statistics.workmanager.NotificationSchedulerProvider
import tmg.testutils.BaseTest

internal class ScheduleControllerTest : BaseTest() {

    private var mockNotificationController: NotificationController = mockk(relaxed = true)
    private var mockUpNextRepository: UpNextRepository = mockk(relaxed = true)
    private var mockApplicationContext: Context = mockk(relaxed = true)
    private var mockScheduleRepository: ScheduleRepository = mockk(relaxed = true)
    private var mockNotificationSchedulerProvider: NotificationSchedulerProvider = mockk(relaxed = true)

    private lateinit var sut: ScheduleController

    @BeforeEach
    internal fun setUp() {
        every { mockUpNextRepository.notificationReminderPeriod } returns NotificationReminder.MINUTES_30

        every { mockUpNextRepository.notificationRace } returns true
        every { mockUpNextRepository.notificationQualifying } returns true
        every { mockUpNextRepository.notificationFreePractice } returns true
        every { mockUpNextRepository.notificationOther } returns true
    }

    private fun initSUT() {
        sut = ScheduleController(mockApplicationContext, mockNotificationController, mockUpNextRepository, mockScheduleRepository, mockNotificationSchedulerProvider)
    }

    @Test
    fun `no next race if remote config is empty`() = coroutineTest {
        coEvery { mockScheduleRepository.getUpcomingEvents(any()) } returns emptyList()
        initSUT()

        runBlockingTest {
            assertNull(sut.getNextEvent())
        }
    }

    @Test
    fun `no next up next if all timestamps are in the past`() = coroutineTest {
        val list = listOf<OverviewRace>()

        coEvery { mockScheduleRepository.getUpcomingEvents(any()) } returns list
        initSUT()

        runBlockingTest {
            assertNull(sut.getNextEvent())
        }
    }

    @Test
    fun `last event shown when last timestamp is available`() = coroutineTest {
        val expected = generateUpNextItem(0 to "12:00", -1 to "12:00")
        val list = listOf(
            expected,
            generateUpNextItem(3 to "10:00")
        )
        coEvery { mockScheduleRepository.getUpcomingEvents(any()) } returns list
        initSUT()

        runBlockingTest {
            assertEquals(expected, sut.getNextEvent())
        }
    }

    @Test
    fun `last event shown when first last timestamp is available`() = coroutineTest {
        val expected = generateUpNextItem(0 to "01:00", 2 to "12:00")
        val list = listOf(
            expected,
            generateUpNextItem(-1 to "10:00")
        )
        coEvery { mockScheduleRepository.getUpcomingEvents(any()) } returns list
        initSUT()

        runBlockingTest {
            assertEquals(expected, sut.getNextEvent())
        }
    }

    @Test
    fun `last event shown when date is between between two last view timestamps`() = coroutineTest {
        val expected = generateUpNextItem(1 to "01:00", 2 to "12:00")
        val list = listOf(
            expected,
            generateUpNextItem(-1 to "10:00")
        )
        coEvery { mockScheduleRepository.getUpcomingEvents(any()) } returns list
        initSUT()

        runBlockingTest {
            assertEquals(expected, sut.getNextEvent())
        }
    }

    @Test
    fun `second last event shown when last of second item is shown with last all in future`() = coroutineTest {
        val expected = generateUpNextItem(-1 to "01:00", 0 to "12:00")
        val list = listOf(
//            generateUpNextItem(-3 to "10:00"),
            expected
        )
        coEvery { mockScheduleRepository.getUpcomingEvents(any()) } returns list
        initSUT()

        runBlockingTest {
            assertEquals(expected, sut.getNextEvent())
        }
    }

    @Test
    fun `list item correct item in middle list of all`() = coroutineTest {
        val expected = generateUpNextItem(0 to "12:00")
        val list = listOf(
//            generateUpNextItem(-4 to "12:00"),
//            generateUpNextItem(-2 to "11:00"),
            expected,
            generateUpNextItem(1 to "11:00"),
            generateUpNextItem(2 to "11:00")
        )

        coEvery { mockScheduleRepository.getUpcomingEvents(any()) } returns list
        initSUT()

        runBlockingTest {
            assertEquals(expected, sut.getNextEvent())
        }
    }

    //region Race simulations

    @Test
    fun `list item is correct when simulating race weekend on race day`() = coroutineTest {
        val list = listOf(
            generateUpNextItem(
                -2 to "11:00",
                -2 to "15:00",
                -1 to "12:00",
                -1 to "15:00",
                0 to "15:00"
            ),
            generateUpNextItem(
                5 to "11:00",
                5 to "15:00",
                6 to "12:00",
                6 to "15:00",
                7 to "15:00"
            )
        )
        initSUT()
        coEvery { mockScheduleRepository.getUpcomingEvents(any()) } returns list

        runBlockingTest {
            assertEquals(list[0], sut.getNextEvent())
        }
    }

    @Test
    fun `list item is correct when simulating race weekend on saturday (qualifying + fp3)`() = coroutineTest {
        val list = listOf(
            generateUpNextItem(
                -1 to "11:00",
                -1 to "15:00",
                0 to "12:00",
                0 to "15:00",
                1 to "15:00"
            ),
            generateUpNextItem(
                5 to "11:00",
                5 to "15:00",
                6 to "12:00",
                6 to "15:00",
                7 to "15:00"
            )
        )
        initSUT()
        coEvery { mockScheduleRepository.getUpcomingEvents(any()) } returns list

        runBlockingTest {
            assertEquals(list[0], sut.getNextEvent())
        }
    }

    @Test
    fun `list item is correct when simulating race weekend on friday (fp1 + fp2)`() = coroutineTest {
        val list = listOf(
            generateUpNextItem(
                0 to "11:00",
                0 to "15:00",
                1 to "12:00",
                1 to "15:00",
                2 to "15:00"
            ),
            generateUpNextItem(
                5 to "11:00",
                5 to "15:00",
                6 to "12:00",
                6 to "15:00",
                7 to "15:00"
            )
        )
        initSUT()
        coEvery { mockScheduleRepository.getUpcomingEvents(any()) } returns list

        runBlockingTest {
            assertEquals(list[0], sut.getNextEvent())
        }
    }

    @Test
    fun `list item is correct when simulating race weekend on thursday (coming up)`() = coroutineTest {
        val list = listOf(
            generateUpNextItem(
                1 to "11:00",
                1 to "15:00",
                2 to "12:00",
                2 to "15:00",
                3 to "15:00"
            ),
            generateUpNextItem(
                5 to "11:00",
                5 to "15:00",
                6 to "12:00",
                6 to "15:00",
                7 to "15:00"
            )
        )
        initSUT()
        coEvery { mockScheduleRepository.getUpcomingEvents(any()) } returns list

        runBlockingTest {
            assertEquals(list[0], sut.getNextEvent())
        }
    }

    @Test
    fun `list item is correct when simulating race weekend on monday (coming up)`() = coroutineTest {
        val list = listOf(
            generateUpNextItem(
                5 to "11:00",
                5 to "15:00",
                6 to "12:00",
                6 to "15:00",
                7 to "15:00"
            )
        )
        initSUT()
        coEvery { mockScheduleRepository.getUpcomingEvents(any()) } returns list

        runBlockingTest {
            assertEquals(list[0], sut.getNextEvent())
        }
    }

    //endregion


    //region Notification preferences - Race

    @Test
    fun `is notification race reads value from preferences repository with default to true`() = coroutineTest {
        every { mockUpNextRepository.notificationRace } returns true

        initSUT()

        assertTrue(sut.notificationRace)
        verify {
            mockUpNextRepository.notificationRace
        }
    }

    @Test
    fun `setting notification race enabled saves value from preferences repository`() = coroutineTest {
        initSUT()

        sut.notificationRace = true
        verify {
            mockUpNextRepository.notificationRace = true
            // TODO: Verify schedule
        }
    }

    //endregion

    //region Notification preferences - Qualifying

    @Test
    fun `is notification qualifying reads value from preferences repository with default to true`() = coroutineTest {
        every { mockUpNextRepository.notificationQualifying } returns true

        initSUT()

        assertTrue(sut.notificationQualifying)
        verify {
            mockUpNextRepository.notificationQualifying
        }
    }

    @Test
    fun `setting notification qualifying enabled saves value from preferences repository`() = coroutineTest {
        initSUT()

        sut.notificationQualifying = true
        verify {
            mockUpNextRepository.notificationQualifying = true
            // TODO: Verify schedule
        }
    }

    //endregion

    //region Notification preferences - Free Practice

    @Test
    fun `is notification free practice reads value from preferences repository with default to true`() = coroutineTest {
        every { mockUpNextRepository.notificationFreePractice } returns true

        initSUT()

        assertTrue(sut.notificationFreePractice)
        verify {
            mockUpNextRepository.notificationFreePractice
        }
    }

    @Test
    fun `setting notification free practice enabled saves value from preferences repository`() = coroutineTest {
        initSUT()

        sut.notificationFreePractice = true
        verify {
            mockUpNextRepository.notificationFreePractice = true
            // TODO: Verify schedule
        }
    }

    //endregion

    //region Notification preferences - Other

    @Test
    fun `is notification other reads value from preferences repository with default to true`() = coroutineTest {
        every { mockUpNextRepository.notificationOther } returns true

        initSUT()

        assertTrue(sut.notificationSeasonInfo)
        verify {
            mockUpNextRepository.notificationOther
        }
    }

    @Test
    fun `setting notification other enabled saves value from preferences repository`() = coroutineTest {
        initSUT()

        sut.notificationSeasonInfo = true
        verify {
            mockUpNextRepository.notificationOther = true
            // TODO: Verify schedule
        }
    }

    //endregion

    //region Notification reminder

    @Test
    fun `is notification reminder period reads value from preferences repository`() = coroutineTest {
        every { mockUpNextRepository.notificationReminderPeriod } returns NotificationReminder.MINUTES_30

        initSUT()

        assertEquals(NotificationReminder.MINUTES_30, sut.notificationReminder)
        verify {
            mockUpNextRepository.notificationReminderPeriod
        }
    }

    @Test
    fun `is notification reminder period writes value from preferences repository`() = coroutineTest {
        initSUT()

        sut.notificationReminder = NotificationReminder.MINUTES_60
        verify {
            mockUpNextRepository.notificationReminderPeriod = NotificationReminder.MINUTES_60
        }
    }

    //endregion

    //region Notification onboarding

    @Test
    fun `notification onboarding reads value from repository`() = coroutineTest {
        every { mockUpNextRepository.seenNotificationOnboarding } returns true

        initSUT()

        assertFalse(sut.shouldShowNotificationOnboarding)
        verify {
            mockUpNextRepository.seenNotificationOnboarding
        }
    }

    @Test
    fun `marking notifications seen marks onboarding seen in repository`() = coroutineTest {
        every { mockUpNextRepository.seenNotificationOnboarding } returns false

        initSUT()

        sut.seenOnboarding()
        verify {
            mockUpNextRepository.seenNotificationOnboarding = true
        }
    }

    //endregion

    //region Schedule notifications

    @Test
    fun `scheduling notification schedules notification`() = coroutineTest {
        initSUT()
        sut.scheduleNotifications()

        verify {
            mockNotificationSchedulerProvider.schedule()
        }
    }

    //endregion

    private fun generateUpNextItem(vararg delta: Pair<Int, String>): OverviewRace {
        return OverviewRace(
            date = LocalDate.now(),
            time = LocalTime.of(0, 12, 0),
            season = 2020,
            round = 1,
            raceName = "Grand Prix 2020 1",
            circuitId = "circuitId",
            circuitName = "circuitName",
            country = "country",
            countryISO = "countryISO",
            hasQualifying = true,
            hasResults = true,
            schedule = delta.map {
                Schedule(
                    label = "Example ${it.second} ${it.first}",
                    date = LocalDate.now().plusDays(it.first.toLong()),
                    time = LocalTime.parse(it.second, DateTimeFormatter.ofPattern("HH:mm"))
                )
            }
        )
    }

}
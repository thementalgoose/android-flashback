package tmg.flashback.upnext.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.Clock
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.model.Timestamp
import tmg.flashback.upnext.model.NotificationReminder
import tmg.flashback.upnext.repository.UpNextRepository
import tmg.flashback.upnext.repository.model.UpNextSchedule
import tmg.flashback.upnext.repository.model.UpNextScheduleTimestamp
import tmg.flashback.upnext.utils.NotificationUtils
import tmg.flashback.upnext.utils.NotificationUtils.getCategoryBasedOnLabel
import tmg.flashback.upnext.utils.NotificationUtils.getRequestCode
import tmg.notifications.controllers.NotificationController
import tmg.testutils.BaseTest

internal class UpNextControllerTest : BaseTest() {

    private var mockNotificationController: NotificationController = mockk(relaxed = true)
    private var mockUpNextRepository: UpNextRepository = mockk(relaxed = true)

    private lateinit var sut: UpNextController

    @BeforeEach
    internal fun setUp() {
        every { mockUpNextRepository.notificationReminderPeriod } returns NotificationReminder.MINUTES_30

        every { mockUpNextRepository.notificationRace } returns true
        every { mockUpNextRepository.notificationQualifying } returns true
        every { mockUpNextRepository.notificationFreePractice } returns true
        every { mockUpNextRepository.notificationOther } returns true
    }

    private fun initSUT() {
        sut = UpNextController(mockNotificationController, mockUpNextRepository)
    }

    @Test
    fun `no next race if remote config is empty`() {
        every { mockUpNextRepository.upNext } returns emptyList()
        initSUT()

        assertNull(sut.getNextEvent())
    }

    @Test
    fun `no next up next if all timestamps are in the past`() {
        val list = listOf(
            generateUpNextItem(-1 to "12:00", -1 to "10:00"),
            generateUpNextItem(-2 to "12:00", -1 to "10:00")
        )

        every { mockUpNextRepository.upNext } returns list
        initSUT()

        assertNull(sut.getNextEvent())
    }

    @Test
    fun `last event shown when last timestamp is available`() {
        val expected = generateUpNextItem(0 to "12:00", -1 to "12:00")
        val list = listOf(
            expected,
            generateUpNextItem(3 to "10:00")
        )
        every { mockUpNextRepository.upNext } returns list
        initSUT()

        assertEquals(expected, sut.getNextEvent())
    }

    @Test
    fun `last event shown when first last timestamp is available`() {
        val expected = generateUpNextItem(0 to "01:00", 2 to "12:00")
        val list = listOf(
            expected,
            generateUpNextItem(-1 to "10:00")
        )
        every { mockUpNextRepository.upNext } returns list
        initSUT()

        assertEquals(expected, sut.getNextEvent())
    }

    @Test
    fun `last event shown when date is between between two last view timestamps`() {
        val expected = generateUpNextItem(1 to "01:00", 2 to "12:00")
        val list = listOf(
            expected,
            generateUpNextItem(-1 to "10:00")
        )
        every { mockUpNextRepository.upNext } returns list
        initSUT()

        assertEquals(expected, sut.getNextEvent())
    }

    @Test
    fun `second last event shown when last of second item is shown with last all in future`() {
        val expected = generateUpNextItem(-1 to "01:00", 0 to "12:00")
        val list = listOf(
            generateUpNextItem(-3 to "10:00"),
            expected
        )
        every { mockUpNextRepository.upNext } returns list
        initSUT()

        assertEquals(expected, sut.getNextEvent())
    }

    @Test
    fun `list item correct item in middle list of all`() {
        val expected = generateUpNextItem(0 to "12:00")
        val list = listOf(
            generateUpNextItem(-4 to "12:00"),
            generateUpNextItem(-2 to "11:00"),
            expected,
            generateUpNextItem(1 to "11:00"),
            generateUpNextItem(2 to "11:00")
        )

        every { mockUpNextRepository.upNext } returns list
        initSUT()

        assertEquals(expected, sut.getNextEvent())
    }

    //region Race simulations

    @Test
    fun `list item is correct when simulating race weekend on race day`() {
        val list = listOf(
            generateUpNextItem(
                -9 to "11:00",
                -9 to "15:00",
                -8 to "12:00",
                -8 to "15:00",
                -7 to "15:00"
            ),
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
        every { mockUpNextRepository.upNext } returns list

        assertEquals(list[1], sut.getNextEvent())
    }

    @Test
    fun `list item is correct when simulating race weekend on saturday (qualifying + fp3)`() {
        val list = listOf(
            generateUpNextItem(
                -9 to "11:00",
                -9 to "15:00",
                -8 to "12:00",
                -8 to "15:00",
                -7 to "15:00"
            ),
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
        every { mockUpNextRepository.upNext } returns list

        assertEquals(list[1], sut.getNextEvent())
    }

    @Test
    fun `list item is correct when simulating race weekend on friday (fp1 + fp2)`() {
        val list = listOf(
            generateUpNextItem(
                -9 to "11:00",
                -9 to "15:00",
                -8 to "12:00",
                -8 to "15:00",
                -7 to "15:00"
            ),
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
        every { mockUpNextRepository.upNext } returns list

        assertEquals(list[1], sut.getNextEvent())
    }

    @Test
    fun `list item is correct when simulating race weekend on thursday (coming up)`() {
        val list = listOf(
            generateUpNextItem(
                -9 to "11:00",
                -9 to "15:00",
                -8 to "12:00",
                -8 to "15:00",
                -7 to "15:00"
            ),
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
        every { mockUpNextRepository.upNext } returns list

        assertEquals(list[1], sut.getNextEvent())
    }

    @Test
    fun `list item is correct when simulating race weekend on monday (coming up)`() {
        val list = listOf(
            generateUpNextItem(
                -9 to "11:00",
                -9 to "15:00",
                -8 to "12:00",
                -8 to "15:00",
                -7 to "15:00"
            ),
            generateUpNextItem(
                -3 to "11:00",
                -3 to "15:00",
                -2 to "12:00",
                -2 to "15:00",
                -1 to "15:00"
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
        every { mockUpNextRepository.upNext } returns list

        assertEquals(list[2], sut.getNextEvent())
    }

    //endregion


    //region Notification preferences - Race

    @Test
    fun `is notification race reads value from preferences repository with default to true`() {
        every { mockUpNextRepository.notificationRace } returns true

        initSUT()

        assertTrue(sut.notificationRace)
        verify {
            mockUpNextRepository.notificationRace
        }
    }

    @Test
    fun `setting notification race enabled saves value from preferences repository`() {
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
    fun `is notification qualifying reads value from preferences repository with default to true`() {
        every { mockUpNextRepository.notificationQualifying } returns true

        initSUT()

        assertTrue(sut.notificationQualifying)
        verify {
            mockUpNextRepository.notificationQualifying
        }
    }

    @Test
    fun `setting notification qualifying enabled saves value from preferences repository`() {
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
    fun `is notification free practice reads value from preferences repository with default to true`() {
        every { mockUpNextRepository.notificationFreePractice } returns true

        initSUT()

        assertTrue(sut.notificationFreePractice)
        verify {
            mockUpNextRepository.notificationFreePractice
        }
    }

    @Test
    fun `setting notification free practice enabled saves value from preferences repository`() {
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
    fun `is notification other reads value from preferences repository with default to true`() {
        every { mockUpNextRepository.notificationOther } returns true

        initSUT()

        assertTrue(sut.notificationSeasonInfo)
        verify {
            mockUpNextRepository.notificationOther
        }
    }

    @Test
    fun `setting notification other enabled saves value from preferences repository`() {
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
    fun `is notification reminder period reads value from preferences repository`() {
        every { mockUpNextRepository.notificationReminderPeriod } returns NotificationReminder.MINUTES_30

        initSUT()

        assertEquals(NotificationReminder.MINUTES_30, sut.notificationReminder)
        verify {
            mockUpNextRepository.notificationReminderPeriod
        }
    }

    //endregion

    //region Notification onboarding

    @Test
    fun `notification onboarding reads value from repository`() {
        every { mockUpNextRepository.seenNotificationOnboarding } returns true

        initSUT()

        assertFalse(sut.shouldShowNotificationOnboarding)
        verify {
            mockUpNextRepository.seenNotificationOnboarding
        }
    }

    @Test
    fun `marking notifications seen marks onboarding seen in repository`() {
        every { mockUpNextRepository.seenNotificationOnboarding } returns false

        initSUT()

        sut.seenOnboarding()
        verify {
            mockUpNextRepository.seenNotificationOnboarding = true
        }
    }

    //endregion

    //region Schedule notification filtering

    @Test
    fun `when finding notifications to schedule it scheduled accurately the local notifications with the manager`() {

        every { mockUpNextRepository.upNext } returns exampleUpNextList

        initSUT()
        sut.scheduleNotifications()

        verify {
            mockNotificationController.cancelAllNotifications()
        }

        verifyScheduleLocal(times = 0, past, 0, past.values[0])
        verifyScheduleLocal(times = 0, past, 1, past.values[1])
        verifyScheduleLocal(times = 0, present, 0, present.values[0])

        // Qualifying while in the future is ahead of 30 minute notice period!
        verifyScheduleLocal(times = 0, present, 1, present.values[1])

        verifyScheduleLocal(times = 1, present, 2, present.values[2])
        verifyScheduleLocal(times = 1, future, 0, future.values[0])
    }

    //endregion

    private fun verifyScheduleLocal(times: Int = 1, upNextSchedule: UpNextSchedule, index: Int, item: UpNextScheduleTimestamp) {
        val channelId = getCategoryBasedOnLabel(item.label).channelId
        var timestampUtc: LocalDateTime? = null
        item.timestamp.on(
            dateAndTime = { utc, local ->
                timestampUtc = utc
            }
        )
        val requestCode = getRequestCode(timestampUtc!!)
        timestampUtc = timestampUtc?.minusMinutes(30)
        verify(exactly = times) {
            mockNotificationController.scheduleLocalNotification(
                requestCode = requestCode,
                channelId = channelId,
                title = "${item.label} starts in 30 minutes",
                text = "${upNextSchedule.title} ${item.label} starts in 30 minutes",
                timestamp = timestampUtc!!
            )
        }
    }

    private val past: UpNextSchedule = generateUpNextItem(
        season = 2020,
        round = 2,
            "qualifying" to LocalDateTime.now(Clock.systemUTC()).minusDays(2L),
            "race" to LocalDateTime.now(Clock.systemUTC()).minusDays(1L)
    )
    private val present: UpNextSchedule = generateUpNextItem(
        season = 2020,
        round = 3,
            "fp3" to LocalDateTime.now(Clock.systemUTC()).minusHours(1L),
            "qualifying" to LocalDateTime.now(Clock.systemUTC()).plusMinutes(5L),
            "race" to LocalDateTime.now(Clock.systemUTC()).plusHours(8L)
    )
    private val future: UpNextSchedule = generateUpNextItem(
        season = 2020,
        round = 4,
            "fp3" to LocalDateTime.now(Clock.systemUTC()).plusDays(1L)
    )
    private val exampleUpNextList: List<UpNextSchedule> = listOf(
        past, present, future
    )


    private fun generateUpNextItem(
        season: Int,
        round: Int,
        vararg delta: Pair<String, LocalDateTime>
    ): UpNextSchedule {
        return UpNextSchedule(
            season = season,
            round = round,
            title = "Grand Prix $season $round",
            subtitle = null,
            values = delta.map { (label, datetime) ->
                UpNextScheduleTimestamp(
                    label = label,
                    timestamp = Timestamp(
                        datetime.toLocalDate(),
                        datetime.toLocalTime()
                    )
                )
            },
            flag = null,
            circuitId = null
        )
    }

    private fun generateUpNextItem(vararg delta: Pair<Int, String>): UpNextSchedule {
        return UpNextSchedule(
            season = 1,
            round = 3,
            title = "Up next item $delta",
            subtitle = null,
            values = delta.map {
                UpNextScheduleTimestamp(
                    label = "Example ${it.second} ${it.first}",
                    timestamp = Timestamp(
                        LocalDate.now().plusDays(it.first.toLong()),
                        LocalTime.parse(it.second, DateTimeFormatter.ofPattern("HH:mm"))
                    )
                )
            },
            flag = null,
            circuitId = null
        )
    }

}
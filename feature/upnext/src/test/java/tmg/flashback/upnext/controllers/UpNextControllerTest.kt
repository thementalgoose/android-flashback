package tmg.flashback.upnext.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.model.Timestamp
import tmg.flashback.upnext.model.NotificationReminder
import tmg.flashback.upnext.repository.UpNextRepository
import tmg.flashback.upnext.repository.model.UpNextSchedule
import tmg.flashback.upnext.repository.model.UpNextScheduleTimestamp
import tmg.notifications.controllers.NotificationController
import tmg.testutils.BaseTest

internal class UpNextControllerTest : BaseTest() {

    private var mockNotificationController: NotificationController = mockk(relaxed = true)
    private var mockUpNextRepository: UpNextRepository = mockk(relaxed = true)

    private lateinit var sut: UpNextController

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

        assertTrue(sut.shouldShowNotificationOnboarding)
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
    fun `when finding notifications to schedule it assigns correct channel ids`() {
        TODO()
    }

    @Test
    fun `when finding notifications to schedule it filters out items in the past`() {
        TODO()
    }

    @Test
    fun `when finding notifications to schedule it filters out those which have a preference to be shown only`() {
        TODO()
    }

    @Test
    fun `when finding notifications to schedule it cancels all notifications as part of the flow`() {
        TODO()
    }

    @Test
    fun `when finding notifications to schedule it scheduled accurately the local notifications with the manager`() {
        TODO()
    }

    //endregion

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
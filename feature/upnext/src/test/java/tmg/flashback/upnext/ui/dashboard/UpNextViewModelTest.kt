package tmg.flashback.upnext.ui.dashboard

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import tmg.flashback.formula1.model.Timestamp
import tmg.flashback.upnext.R
import tmg.flashback.upnext.controllers.UpNextController
import tmg.flashback.upnext.repository.model.UpNextSchedule
import tmg.flashback.upnext.repository.model.UpNextScheduleTimestamp
import tmg.flashback.upnext.ui.timezone.TimezoneItem
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test

internal class UpNextViewModelTest: BaseTest() {

    private val mockUpNextController: UpNextController = mockk(relaxed = true)

    private lateinit var sut: UpNextViewModel

    private fun initSUT() {
        sut = UpNextViewModel(mockUpNextController)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockUpNextController.notificationRace } returns true
        every { mockUpNextController.notificationQualifying } returns true
        every { mockUpNextController.notificationFreePractice } returns false
        every { mockUpNextController.notificationSeasonInfo } returns false
        every { mockUpNextController.getNextEvent() } returns mockUpNext
    }

    //region Up Next item

    @Test
    fun `data output returns full next item event`() {
        initSUT()

        sut.outputs.data.test {
            assertValue(mockUpNext)
        }
    }

    @Test
    fun `content returns ordered list based on values with individual items on each day`() {
        val practice: LocalDateTime = LocalDateTime.of(2020, 1, 1, 1, 0, 0)
        val practiceTimestamp = Timestamp(
            originalDate = practice.toLocalDate(),
            originalTime = practice.toLocalTime(),
            zone = ZoneId.systemDefault()
        )
        val qualifying: LocalDateTime = LocalDateTime.of(2020, 1, 2, 1, 0, 0)
        val qualifyingTimestamp = Timestamp(
            originalDate = qualifying.toLocalDate(),
            originalTime = qualifying.toLocalTime(),
            zone = ZoneId.systemDefault()
        )
        val race: LocalDateTime = LocalDateTime.of(2020, 1, 3, 1, 0, 0)
        val raceTimestamp = Timestamp(
            originalDate = race.toLocalDate(),
            originalTime = race.toLocalTime(),
            zone = ZoneId.systemDefault()
        )
        every { mockUpNextController.getNextEvent() } returns mockUpNext.copy(values = listOf(
            UpNextScheduleTimestamp("qualifying", qualifyingTimestamp),
            UpNextScheduleTimestamp("race", raceTimestamp),
            UpNextScheduleTimestamp("practice", practiceTimestamp)
        ))

        initSUT()

        sut.outputs.content.test {
            assertValue(listOf(
                UpNextBreakdownModel.Divider,
                UpNextBreakdownModel.Day("Wednesday 1st January", LocalDate.of(2020, 1, 1)),
                UpNextBreakdownModel.Item("practice", practiceTimestamp, false),
                UpNextBreakdownModel.Divider,
                UpNextBreakdownModel.Day("Thursday 2nd January", LocalDate.of(2020, 1, 2)),
                UpNextBreakdownModel.Item("qualifying", qualifyingTimestamp, true),
                UpNextBreakdownModel.Divider,
                UpNextBreakdownModel.Day("Friday 3rd January", LocalDate.of(2020, 1, 3)),
                UpNextBreakdownModel.Item("race", raceTimestamp, true)
            ))
        }
    }


    @Test
    fun `content returns ordered list based on values with two items on one day`() {
        val practice: LocalDateTime = LocalDateTime.of(2020, 1, 1, 1, 0, 0)
        val practiceTimestamp = Timestamp(
            originalDate = practice.toLocalDate(),
            originalTime = practice.toLocalTime(),
            zone = ZoneId.systemDefault()
        )
        val qualifying: LocalDateTime = LocalDateTime.of(2020, 1, 2, 1, 0, 0)
        val qualifyingTimestamp = Timestamp(
            originalDate = qualifying.toLocalDate(),
            originalTime = qualifying.toLocalTime(),
            zone = ZoneId.systemDefault()
        )
        val race: LocalDateTime = LocalDateTime.of(2020, 1, 2, 1, 0, 0)
        val raceTimestamp = Timestamp(
            originalDate = race.toLocalDate(),
            originalTime = race.toLocalTime(),
            zone = ZoneId.systemDefault()
        )
        every { mockUpNextController.getNextEvent() } returns mockUpNext.copy(values = listOf(
            UpNextScheduleTimestamp("qualifying", qualifyingTimestamp),
            UpNextScheduleTimestamp("race", raceTimestamp),
            UpNextScheduleTimestamp("practice", practiceTimestamp)
        ))

        initSUT()

        sut.outputs.content.test {
            assertValue(listOf(
                UpNextBreakdownModel.Divider,
                UpNextBreakdownModel.Day("Wednesday 1st January", LocalDate.of(2020, 1, 1)),
                UpNextBreakdownModel.Item("practice", practiceTimestamp, false),
                UpNextBreakdownModel.Divider,
                UpNextBreakdownModel.Day("Thursday 2nd January", LocalDate.of(2020, 1, 2)),
                UpNextBreakdownModel.Item("qualifying", qualifyingTimestamp, true),
                UpNextBreakdownModel.Item("race", raceTimestamp, true)
            ))
        }
    }


    @Test
    fun `content returns ordered list based on values with all items on one day`() {
        val practice: LocalDateTime = LocalDateTime.of(2023, 1, 9, 1, 0, 0)
        val practiceTimestamp = Timestamp(
            originalDate = practice.toLocalDate(),
            originalTime = practice.toLocalTime(),
            zone = ZoneId.systemDefault()
        )
        val qualifying: LocalDateTime = LocalDateTime.of(2023, 1, 9, 2, 0, 0)
        val qualifyingTimestamp = Timestamp(
            originalDate = qualifying.toLocalDate(),
            originalTime = qualifying.toLocalTime(),
            zone = ZoneId.systemDefault()
        )
        val race: LocalDateTime = LocalDateTime.of(2023, 1, 9, 3, 0, 0)
        val raceTimestamp = Timestamp(
            originalDate = race.toLocalDate(),
            originalTime = race.toLocalTime(),
            zone = ZoneId.systemDefault()
        )
        every { mockUpNextController.getNextEvent() } returns mockUpNext.copy(values = listOf(
            UpNextScheduleTimestamp("qualifying", qualifyingTimestamp),
            UpNextScheduleTimestamp("race", raceTimestamp),
            UpNextScheduleTimestamp("practice", practiceTimestamp)
        ))

        initSUT()

        sut.outputs.content.test {
            assertValue(listOf(
                UpNextBreakdownModel.Divider,
                UpNextBreakdownModel.Day("Monday 9th January", LocalDate.of(2023, 1, 9)),
                UpNextBreakdownModel.Item("practice", practiceTimestamp, false),
                UpNextBreakdownModel.Item("qualifying", qualifyingTimestamp, true),
                UpNextBreakdownModel.Item("race", raceTimestamp, true)
            ))
        }
    }

    @Test
    fun `timezones output returns single timezone`() {
        initSUT()

        sut.outputs.timezones.test {
            assertValue(listOf(TimezoneItem(R.string.dashboard_up_next_your_time)))
        }
    }

    //endregion

    //region Remaining days

    @Test
    fun `remaining days is taken of minimum event time`() {
        val practice: LocalDateTime = LocalDateTime.now().plusDays(7L)
        val practiceTimestamp = Timestamp(
            originalDate = practice.toLocalDate(),
            originalTime = practice.toLocalTime(),
            zone = ZoneId.systemDefault()
        )
        val qualifying: LocalDateTime = LocalDateTime.now().plusDays(9L)
        val qualifyingTimestamp = Timestamp(
            originalDate = qualifying.toLocalDate(),
            originalTime = qualifying.toLocalTime(),
            zone = ZoneId.systemDefault()
        )

        every { mockUpNextController.getNextEvent() } returns mockUpNext.copy(values = listOf(
            UpNextScheduleTimestamp("practice", practiceTimestamp),
            UpNextScheduleTimestamp("qualifying", qualifyingTimestamp),
        ))

        initSUT()

        sut.outputs.remainingDays.test {
            assertValue(7)
        }
    }

    @Test
    fun `remaining days is capped at 0 if minimum event time has already passed`() {
        val practice: LocalDateTime = LocalDateTime.now().minusDays(1L)
        val practiceTimestamp = Timestamp(
            originalDate = practice.toLocalDate(),
            originalTime = practice.toLocalTime(),
            zone = ZoneId.systemDefault()
        )
        val qualifying: LocalDateTime = LocalDateTime.now().plusDays(9L)
        val qualifyingTimestamp = Timestamp(
            originalDate = qualifying.toLocalDate(),
            originalTime = qualifying.toLocalTime(),
            zone = ZoneId.systemDefault()
        )

        every { mockUpNextController.getNextEvent() } returns mockUpNext.copy(values = listOf(
            UpNextScheduleTimestamp("practice", practiceTimestamp),
            UpNextScheduleTimestamp("qualifying", qualifyingTimestamp),
        ))

        initSUT()

        sut.outputs.remainingDays.test {
            assertValue(0)
        }
    }

    @Test
    fun `remaining days is set to 0 is minumim is today`() {
        val practice: LocalDateTime = LocalDateTime.now()
        val practiceTimestamp = Timestamp(
            originalDate = practice.toLocalDate(),
            originalTime = practice.toLocalTime(),
            zone = ZoneId.systemDefault()
        )
        val qualifying: LocalDateTime = LocalDateTime.now().plusDays(9L)
        val qualifyingTimestamp = Timestamp(
            originalDate = qualifying.toLocalDate(),
            originalTime = qualifying.toLocalTime(),
            zone = ZoneId.systemDefault()
        )

        every { mockUpNextController.getNextEvent() } returns mockUpNext.copy(values = listOf(
            UpNextScheduleTimestamp("practice", practiceTimestamp),
            UpNextScheduleTimestamp("qualifying", qualifyingTimestamp),
        ))

        initSUT()

        sut.outputs.remainingDays.test {
            assertValue(0)
        }
    }

    //endregion

    //region Empty Up Next

    @Test
    fun `data value is placeholder schedule model when next event is null`() {
        every { mockUpNextController.getNextEvent() } returns null
        initSUT()
        sut.outputs.data.test {
            assertValue(UpNextSchedule(
                title = "Nothing here yet!",
                subtitle = "Please check back later when we know more",
                values = emptyList(),
                flag = null,
                circuitId = null,
                season = 0,
                round = 0
            ))
        }
    }

    @Test
    fun `content value is empty list when next event is null`() {
        every { mockUpNextController.getNextEvent() } returns null
        initSUT()
        sut.outputs.content.test {
            assertValue(emptyList())
        }
    }

    @Test
    fun `timezone value is empty list when next event is null`() {
        every { mockUpNextController.getNextEvent() } returns null
        initSUT()
        sut.outputs.timezones.test {
            assertValue(emptyList())
        }
    }

    @Test
    fun `remaining days is 0 when next event is null`() {
        every { mockUpNextController.getNextEvent() } returns null
        initSUT()
        sut.outputs.remainingDays.test {
            assertValue(0)
        }
    }

    //endregion

    private val mockUpNext: UpNextSchedule = UpNextSchedule(
        season = 1,
        round = 2,
        title = "3",
        subtitle = "4",
        values = emptyList(),
        flag = "6",
        circuitId = "7"
    )
}
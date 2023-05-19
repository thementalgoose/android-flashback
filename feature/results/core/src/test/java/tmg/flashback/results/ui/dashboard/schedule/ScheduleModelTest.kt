package tmg.flashback.results.ui.dashboard.schedule

import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.model

internal class ScheduleModelTest {

    @Test
    fun `should show schedule list returns true when requested and schedule not empty`() {
        val item = ScheduleModel.RaceWeek(
            model = OverviewRace.model(),
            notificationSchedule = mockk(),
            showScheduleList = true
        )

        Assertions.assertTrue(item.shouldShowScheduleList)
    }

    @Test
    fun `should show schedule list returns false when requested and schedule empty`() {
        val item = ScheduleModel.RaceWeek(
            model = OverviewRace.model(schedule = emptyList()),
            notificationSchedule = mockk(),
            showScheduleList = true
        )

        Assertions.assertFalse(item.shouldShowScheduleList)
    }

    @Test
    fun `should show schedule list returns false when not requested and schedule empty`() {
        val item = ScheduleModel.RaceWeek(
            model = OverviewRace.model(),
            notificationSchedule = mockk(),
            showScheduleList = false
        )

        Assertions.assertFalse(item.shouldShowScheduleList)
    }



    @Test
    fun `fade item returns true if item is in future and doesnt have schedule`() {
        val item = ScheduleModel.RaceWeek(
            model = OverviewRace.model(
                date = LocalDate.now().plusDays(1L),
                schedule = emptyList()
            ),
            notificationSchedule = mockk(),
            showScheduleList = true
        )

        Assertions.assertTrue(item.fadeItem)
    }

    @Test
    fun `fade item returns false if item is in future and has showing schedule`() {
        val item = ScheduleModel.RaceWeek(
            model = OverviewRace.model(
                date = LocalDate.now().plusDays(1L),
            ),
            notificationSchedule = mockk(),
            showScheduleList = true
        )

        Assertions.assertFalse(item.fadeItem)
    }

    @Test
    fun `fade item returns false if item is today`() {
        val item = ScheduleModel.RaceWeek(
            model = OverviewRace.model(
                date = LocalDate.now(),
            ),
            notificationSchedule = mockk(),
            showScheduleList = true
        )

        Assertions.assertFalse(item.fadeItem)
    }

    @Test
    fun `fade item returns false if item is in past`() {
        val item = ScheduleModel.RaceWeek(
            model = OverviewRace.model(
                date = LocalDate.now().minusDays(1L),
            ),
            notificationSchedule = mockk(),
            showScheduleList = true
        )

        Assertions.assertFalse(item.fadeItem)
    }
}
package tmg.flashback.stats.ui.dashboard.calendar

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.model

internal class CalendarModelTest {


    @Test
    fun `should show schedule list returns true when requested and schedule not empty`() {
        val item = CalendarModel.List(
            model = OverviewRace.model(),
            showScheduleList = true
        )

        assertTrue(item.shouldShowScheduleList)
    }

    @Test
    fun `should show schedule list returns false when requested and schedule empty`() {
        val item = CalendarModel.List(
            model = OverviewRace.model(schedule = emptyList()),
            showScheduleList = true
        )

        assertFalse(item.shouldShowScheduleList)
    }

    @Test
    fun `should show schedule list returns false when not requested and schedule empty`() {
        val item = CalendarModel.List(
            model = OverviewRace.model(),
            showScheduleList = false
        )

        assertFalse(item.shouldShowScheduleList)
    }



    @Test
    fun `fade item returns true if item is in future and doesnt have schedule`() {
        val item = CalendarModel.List(
            model = OverviewRace.model(
                date = LocalDate.now().plusDays(1L),
                schedule = emptyList()
            ),
            showScheduleList = true
        )

        assertTrue(item.fadeItem)
    }

    @Test
    fun `fade item returns false if item is in future and has showing schedule`() {
        val item = CalendarModel.List(
            model = OverviewRace.model(
                date = LocalDate.now().plusDays(1L),
            ),
            showScheduleList = true
        )

        assertFalse(item.fadeItem)
    }

    @Test
    fun `fade item returns false if item is today`() {
        val item = CalendarModel.List(
            model = OverviewRace.model(
                date = LocalDate.now(),
            ),
            showScheduleList = true
        )

        assertFalse(item.fadeItem)
    }

    @Test
    fun `fade item returns false if item is in past`() {
        val item = CalendarModel.List(
            model = OverviewRace.model(
                date = LocalDate.now().minusDays(1L),
            ),
            showScheduleList = true
        )

        assertFalse(item.fadeItem)
    }
}
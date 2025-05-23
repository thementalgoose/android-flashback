package tmg.flashback.season.presentation.dashboard.races

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate
import tmg.flashback.formula1.model.Event
import tmg.flashback.formula1.model.Overview
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.model
import tmg.flashback.formula1.model.notifications.NotificationSchedule

internal class RacesModelBuilderTest {

    internal val racePreviousWeek4 = OverviewRace.model(date = LocalDate.now().minusDays(4 * 7), round = 1)
    internal val racePreviousWeek3 = OverviewRace.model(date = LocalDate.now().minusDays(3 * 7), round = 2)
    internal val racePreviousWeek2 = OverviewRace.model(date = LocalDate.now().minusDays(2 * 7), round = 3)
    internal val racePreviousWeek1 = OverviewRace.model(date = LocalDate.now().minusDays(1 * 7), round = 4)
    internal val raceThisWeek = OverviewRace.model(date = LocalDate.now(), round = 5)
    internal val raceNextWeek1 = OverviewRace.model(date = LocalDate.now().plusDays(7 * 1), round = 6)
    internal val raceNextWeek2 = OverviewRace.model(date = LocalDate.now().plusDays(7 * 2), round = 7)
    internal val raceNextWeek3 = OverviewRace.model(date = LocalDate.now().plusDays(7 * 3), round = 8)
    internal val raceNextWeek4 = OverviewRace.model(date = LocalDate.now().plusDays(7 * 4), round = 4)

    private val event = Event.model()

    private val fakeNotificationSchedule = NotificationSchedule(
        freePractice = true,
        qualifying = false,
        sprint = true,
        sprintQualifying = true,
        race = false,
        other = true
    )

    private fun initResult(
        overview: Overview,
        events: List<Event> = listOf(event),
        collapse: Boolean = false,
        empty: Boolean = false
    ): List<RacesModel> {
        return RacesModelBuilder.generateScheduleModel(overview.copy(
            overviewRaces = overview.overviewRaces
                .sortedBy { it.date }
                .mapIndexed { index, it ->
                    it.copy(round = index + 1)
                }
            ),
            events = events,
            notificationSchedule = fakeNotificationSchedule,
            showCollapsePreviousRaces = collapse,
            showEmptyWeeks = empty
        )
    }

    @Test
    fun `events then races displayed when completed races is 0 and event is in future`() {
        val overview = Overview.model(
            overviewRaces = listOf(raceNextWeek1),
        )
        val event = event.copy(date = LocalDate.now().plusDays(3))

        val result = initResult(
            overview = overview,
            events = listOf(event)
        )

        assertEquals(2, result.size)
        assertEquals(RacesModel.Event(event), result[0])
        assertEquals(RacesModel.RaceWeek(raceNextWeek1.atRound(1), true, fakeNotificationSchedule), result[1])
    }

    @Test
    fun `events not displayed when completed races is 0 and event is in past`() {
        val overview = Overview.model(
            overviewRaces = listOf(raceNextWeek1),
        )

        val result = initResult(
            overview = overview,
            events = listOf(event.copy(date = LocalDate.now().minusDays(3)))
        )

        assertEquals(1, result.size)
        assertEquals(RacesModel.RaceWeek(raceNextWeek1.atRound(1), true, fakeNotificationSchedule), result[0])
    }

    @Test
    fun `list of races displayed when no upcoming races`() {
        val overview = Overview.model(
            overviewRaces = listOf(racePreviousWeek1, racePreviousWeek2)
        )

        val result = initResult(overview, emptyList())

        assertEquals(2, result.size)
        // Wrong order here shows sorting by date is working
        assertEquals(RacesModel.RaceWeek(racePreviousWeek2.atRound(1), false, fakeNotificationSchedule), result[0])
        assertEquals(RacesModel.RaceWeek(racePreviousWeek1.atRound(2), false, fakeNotificationSchedule), result[1])
    }

    @Test
    fun `collapsible race true with second race upcoming shows race list`() {
        val overview = Overview.model(
            overviewRaces = listOf(racePreviousWeek4, racePreviousWeek2, raceNextWeek1)
        )

        val result = initResult(overview, emptyList(), collapse = true)

        assertEquals(3, result.size)
        assertEquals(RacesModel.RaceWeek(racePreviousWeek4.atRound(1), false, fakeNotificationSchedule), result[0])
        assertEquals(RacesModel.RaceWeek(racePreviousWeek2.atRound(2), false, fakeNotificationSchedule), result[1])
        assertEquals(RacesModel.RaceWeek(raceNextWeek1.atRound(3), true, fakeNotificationSchedule), result[2])
    }

    @Test
    fun `collapsible race false with second race upcoming shows race list`() {
        val overview = Overview.model(
            overviewRaces = listOf(racePreviousWeek4, racePreviousWeek2, raceNextWeek1)
        )

        val result = initResult(overview, emptyList(), collapse = false)

        assertEquals(3, result.size)
        assertEquals(RacesModel.RaceWeek(racePreviousWeek4.atRound(1), false, fakeNotificationSchedule), result[0])
        assertEquals(RacesModel.RaceWeek(racePreviousWeek2.atRound(2), false, fakeNotificationSchedule), result[1])
        assertEquals(RacesModel.RaceWeek(raceNextWeek1.atRound(3), true, fakeNotificationSchedule), result[2])
    }

    @Test
    fun `collapsible race true with third race upcoming shows race list`() {
        val overview = Overview.model(
            overviewRaces = listOf(racePreviousWeek4, racePreviousWeek3, racePreviousWeek2, raceNextWeek1)
        )

        val result = initResult(overview, emptyList(), collapse = true)

        assertEquals(3, result.size)
        assertEquals(
            RacesModel.GroupedCompletedRaces(
            racePreviousWeek4.atRound(1),
            racePreviousWeek3.atRound(2)
        ), result[0])
        assertEquals(RacesModel.RaceWeek(racePreviousWeek2.atRound(3), false, fakeNotificationSchedule), result[1])
        assertEquals(RacesModel.RaceWeek(raceNextWeek1.atRound(4), true, fakeNotificationSchedule), result[2])
    }

    @Test
    fun `collapsible race false with third race upcoming shows race list`() {
        val overview = Overview.model(
            overviewRaces = listOf(racePreviousWeek4, racePreviousWeek3, racePreviousWeek2, raceNextWeek1)
        )

        val result = initResult(overview, emptyList(), collapse = false)

        assertEquals(4, result.size)
        assertEquals(RacesModel.RaceWeek(racePreviousWeek4.atRound(1), false, fakeNotificationSchedule), result[0])
        assertEquals(RacesModel.RaceWeek(racePreviousWeek3.atRound(2), false, fakeNotificationSchedule), result[1])
        assertEquals(RacesModel.RaceWeek(racePreviousWeek2.atRound(3), false, fakeNotificationSchedule), result[2])
        assertEquals(RacesModel.RaceWeek(raceNextWeek1.atRound(4), true, fakeNotificationSchedule), result[3])
    }

    @Test
    fun `collapsible race true with show weeks split shows weeks dividers`() {
        val overview = Overview.model(
            overviewRaces = listOf(racePreviousWeek4, racePreviousWeek3, racePreviousWeek2, raceNextWeek1, raceNextWeek3, raceNextWeek4)
        )

        val result = initResult(overview, emptyList(), collapse = true, empty = true)

        assertEquals(8, result.size)
        assertEquals(
            RacesModel.GroupedCompletedRaces(
            racePreviousWeek4.atRound(1),
            racePreviousWeek3.atRound(2)
        ), result[0])
        assertEquals(RacesModel.RaceWeek(racePreviousWeek2.atRound(3), false, fakeNotificationSchedule), result[1])
        assertTrue(result[2] is RacesModel.EmptyWeek)
        assertTrue(result[3] is RacesModel.EmptyWeek)
        assertEquals(RacesModel.RaceWeek(raceNextWeek1.atRound(4), true, fakeNotificationSchedule), result[4])
        assertTrue(result[5] is RacesModel.EmptyWeek)
        assertEquals(RacesModel.RaceWeek(raceNextWeek3.atRound(5), false, fakeNotificationSchedule), result[6])
        assertEquals(RacesModel.RaceWeek(raceNextWeek4.atRound(6), false, fakeNotificationSchedule), result[7])
    }

    @Test
    fun `collapsible race false with show weeks split shows weeks dividers`() {
        val overview = Overview.model(
            overviewRaces = listOf(racePreviousWeek4, racePreviousWeek2, racePreviousWeek1, raceNextWeek1, raceNextWeek3, raceNextWeek4)
        )

        val result = initResult(overview, emptyList(), collapse = false, empty = true)

        assertEquals(9, result.size)
        assertEquals(RacesModel.RaceWeek(racePreviousWeek4.atRound(1), false, fakeNotificationSchedule), result[0])
        assertTrue(result[1] is RacesModel.EmptyWeek)
        assertEquals(RacesModel.RaceWeek(racePreviousWeek2.atRound(2), false, fakeNotificationSchedule), result[2])
        assertEquals(RacesModel.RaceWeek(racePreviousWeek1.atRound(3), false, fakeNotificationSchedule), result[3])
        assertTrue(result[4] is RacesModel.EmptyWeek)
        assertEquals(RacesModel.RaceWeek(raceNextWeek1.atRound(4), true, fakeNotificationSchedule), result[5])
        assertTrue(result[6] is RacesModel.EmptyWeek)
        assertEquals(RacesModel.RaceWeek(raceNextWeek3.atRound(5), false, fakeNotificationSchedule), result[7])
        assertEquals(RacesModel.RaceWeek(raceNextWeek4.atRound(6), false, fakeNotificationSchedule), result[8])
    }

    private fun OverviewRace.atRound(round: Int): OverviewRace {
        return this.copy(round = round)
    }
}
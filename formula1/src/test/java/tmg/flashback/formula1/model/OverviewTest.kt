package tmg.flashback.formula1.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate

internal class OverviewTest {

    @Test
    fun `SeasonOverview completed returns correct number of rounds`() {

        val model = Overview.model(overviewRaces = listOf(
            OverviewRace.model(
                raceName = "day before",
                date = LocalDate.now().minusDays(1)
            ),
            OverviewRace.model(
                raceName = "today",
                date = LocalDate.now()
            ),
            OverviewRace.model(
                raceName = "day after",
                date = LocalDate.now().plusDays(1)
            )
        ))

        assertEquals(1, model.completed)
    }

    @Test
    fun `SeasonOverview upcoming returns correct number of rounds`() {

        val model = Overview.model(overviewRaces = listOf(
            OverviewRace.model(
                raceName = "day before",
                date = LocalDate.now().minusDays(1)
            ),
            OverviewRace.model(
                raceName = "today",
                date = LocalDate.now()
            ),
            OverviewRace.model(
                raceName = "day after",
                date = LocalDate.now().plusDays(1)
            )
        ))

        assertEquals(2, model.upcoming)
    }

    @Test
    fun `SeasonOverview scheduledToday returns correct number of rounds`() {

        val model = Overview.model(overviewRaces = listOf(
            OverviewRace.model(
                raceName = "day before",
                date = LocalDate.now().minusDays(1)
            ),
            OverviewRace.model(
                raceName = "today",
                date = LocalDate.now()
            ),
            OverviewRace.model(
                raceName = "day after",
                date = LocalDate.now().plusDays(1)
            )
        ))

        assertEquals(1, model.scheduledToday)
    }
}
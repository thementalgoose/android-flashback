package tmg.flashback.formula1.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class RaceDriverOverviewTest {

    @Test
    fun `qualified uses race value when all fields available`() {
        val model = RaceDriverOverview.model(
            q1 = RaceQualifyingRoundDriver.Qualifying.model(position = 11),
            q2 = RaceQualifyingRoundDriver.Qualifying.model(position = 12),
            q3 = RaceQualifyingRoundDriver.Qualifying.model(position = 13),
            qSprint = RaceQualifyingRoundDriver.SprintQualifying.model(finished = 14),
            race = RaceRaceResult.model(qualified = 2)
        )

        assertEquals(2, model.qualified)
    }

    @Test
    fun `qualified uses sprint value when race unavailable`() {
        val model = RaceDriverOverview.model(
            q1 = RaceQualifyingRoundDriver.Qualifying.model(position = 11),
            q2 = RaceQualifyingRoundDriver.Qualifying.model(position = 12),
            q3 = RaceQualifyingRoundDriver.Qualifying.model(position = 13),
            qSprint = RaceQualifyingRoundDriver.SprintQualifying.model(finished = 14),
            race = null
        )

        assertEquals(14, model.qualified)
    }

    @Test
    fun `qualified uses q3 when race unavailable`() {
        val model = RaceDriverOverview.model(
            q1 = RaceQualifyingRoundDriver.Qualifying.model(position = 11),
            q2 = RaceQualifyingRoundDriver.Qualifying.model(position = 12),
            q3 = RaceQualifyingRoundDriver.Qualifying.model(position = 13),
            qSprint = null,
            race = null
        )

        assertEquals(13, model.qualified)
    }

    @Test
    fun `qualified uses q2 when race unavailable`() {
        val model = RaceDriverOverview.model(
            q1 = RaceQualifyingRoundDriver.Qualifying.model(position = 11),
            q2 = RaceQualifyingRoundDriver.Qualifying.model(position = 12),
            q3 = null,
            qSprint = null,
            race = null
        )

        assertEquals(12, model.qualified)
    }

    @Test
    fun `qualified uses q1 when race unavailable`() {
        val model = RaceDriverOverview.model(
            q1 = RaceQualifyingRoundDriver.Qualifying.model(position = 11),
            q2 = null,
            q3 = null,
            qSprint = null,
            race = null
        )

        assertEquals(11, model.qualified)
    }
}
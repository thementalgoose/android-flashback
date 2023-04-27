package tmg.flashback.formula1.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class RaceDriverOverviewTest {

    @Test
    fun `official qualifying position uses race value when all fields available`() {
        val model = RaceDriverOverview.model(
            q1 = QualifyingResult.model(position = 11),
            q2 = QualifyingResult.model(position = 12),
            q3 = QualifyingResult.model(position = 13),
            qSprint = SprintRaceResult.model(finish = 14),
            race = RaceResult.model(qualified = 2)
        )

        assertEquals(2, model.officialQualifyingPosition)
    }

    @Test
    fun `official qualifying position uses sprint value when race missing`() {
        val model = RaceDriverOverview.model(
            q1 = QualifyingResult.model(position = 11),
            q2 = QualifyingResult.model(position = 12),
            q3 = QualifyingResult.model(position = 13),
            qSprint = SprintRaceResult.model(finish = 14),
            race = RaceResult.model(qualified = null)
        )

        assertEquals(14, model.officialQualifyingPosition)
    }

    @Test
    fun `qualified uses q3 when race unavailable`() {
        val model = RaceDriverOverview.model(
            q1 = QualifyingResult.model(position = 11),
            q2 = QualifyingResult.model(position = 12),
            q3 = QualifyingResult.model(position = 13),
            qSprint = null,
            race = null
        )

        assertEquals(13, model.qualified)
    }

    @Test
    fun `qualified uses q2 when race unavailable`() {
        val model = RaceDriverOverview.model(
            q1 = QualifyingResult.model(position = 11),
            q2 = QualifyingResult.model(position = 12),
            q3 = null,
            qSprint = null,
            race = null
        )

        assertEquals(12, model.qualified)
    }

    @Test
    fun `qualified uses q1 when race unavailable`() {
        val model = RaceDriverOverview.model(
            q1 = QualifyingResult.model(position = 11),
            q2 = null,
            q3 = null,
            qSprint = null,
            race = null
        )

        assertEquals(11, model.qualified)
    }
}
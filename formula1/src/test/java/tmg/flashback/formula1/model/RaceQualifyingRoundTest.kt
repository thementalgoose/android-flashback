package tmg.flashback.formula1.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class RaceQualifyingRoundTest {

    @Test
    fun `is sprint qualifying returns true if models have sprint qualifying`() {
        val model = RaceQualifyingRound.model(
            results = listOf(RaceQualifyingRoundDriver.SprintQualifying.model())
        )

        assertTrue(model.isSprintQualifying)
    }

    @Test
    fun `is sprint qualifying returns false if models dont have sprint qualifying`() {
        val model = RaceQualifyingRound.model(
            results = listOf(RaceQualifyingRoundDriver.Qualifying.model())
        )

        assertFalse(model.isSprintQualifying)
    }
}
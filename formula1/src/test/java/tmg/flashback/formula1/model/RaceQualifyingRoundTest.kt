package tmg.flashback.formula1.model

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class RaceQualifyingRoundTest {

    @Test
    fun `is sprint qualifying returns true if models have sprint qualifying`() {
        val model = RaceQualifyingRound.model(
            results = listOf(RaceQualifyingResult.SprintQualifying.model())
        )

        assertTrue(model.isSprintQualifying)
    }

    @Test
    fun `is sprint qualifying returns false if models dont have sprint qualifying`() {
        val model = RaceQualifyingRound.model(
            results = listOf(RaceQualifyingResult.Qualifying.model())
        )

        assertFalse(model.isSprintQualifying)
    }
}
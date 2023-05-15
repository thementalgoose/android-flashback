package tmg.flashback.formula1.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource


internal class SeasonDriverStandingSeasonTest {

    @ParameterizedTest(name = "has valid championship position returns {1} when position is {0}")
    @CsvSource(
        ",false",
        "0,false",
        "1,true",
        "5,true",
        "15,true"
    )
    fun `has valid championship position returns based on position`(championshipPosition: Int?, expectedResult: Boolean) {
        val model = SeasonDriverStandingSeason.model(championshipPosition = championshipPosition)

        assertEquals(expectedResult, model.hasValidChampionshipPosition)
    }

    @Test
    fun `in progress content returns value when round is in progress`() {
        val model = SeasonDriverStandingSeason.model(
            inProgress = true,
            inProgressName = "PROGRESS",
            inProgressRound = 1
        )

        assertEquals(Pair("PROGRESS", 1), model.inProgressContent)
    }

    @Test
    fun `in progress content returns null when in progress is false`() {
        val model = SeasonDriverStandingSeason.model(
            inProgress = false,
            inProgressName = "PROGRESS",
            inProgressRound = 1
        )

        assertNull(model.inProgressContent)
    }
}
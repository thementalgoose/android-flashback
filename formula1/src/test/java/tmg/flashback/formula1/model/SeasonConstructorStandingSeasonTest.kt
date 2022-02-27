package tmg.flashback.formula1.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class SeasonConstructorStandingSeasonTest {

    @ParameterizedTest(name = "has valid championship position returns {1} when position is {0}")
    @CsvSource(
        ",false",
        "0,false",
        "1,true",
        "5,true",
        "15,true"
    )
    fun `has valid championship position returns based on position`(championshipPosition: Int?, expectedResult: Boolean) {
        val model = SeasonConstructorStandingSeason.model(championshipPosition = championshipPosition)

        assertEquals(expectedResult, model.hasValidChampionshipPosition)
    }
}
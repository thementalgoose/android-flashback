package tmg.flashback.ui.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.testutils.BaseTest

internal class PodiumUtilsKtTest: BaseTest() {

    @ParameterizedTest(name = "{0} as ordinal abbreviation is {1}")
    @CsvSource(
            "1,1st",
            "2,2nd",
            "3,3rd",
            "11,11th",
            "21,21st",
            "23,23rd",
            "31,31st"
    )
    fun `PodiumUtils position int to podium`(position: Int, expectedValue: String) {

        assertEquals(expectedValue, position.position())
    }
}
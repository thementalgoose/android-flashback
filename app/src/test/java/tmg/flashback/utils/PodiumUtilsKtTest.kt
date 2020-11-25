package tmg.flashback.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.rss.testutils.BaseTest

class PodiumUtilsKtTest: BaseTest() {

    @ParameterizedTest
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
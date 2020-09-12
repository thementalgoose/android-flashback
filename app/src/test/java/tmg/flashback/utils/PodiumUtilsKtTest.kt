package tmg.flashback.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.testutils.BaseTest

class PodiumUtilsKtTest: BaseTest() {

    @ParameterizedTest
    @CsvSource(
            "1,true,1st",
            "2,true,2nd",
            "3,true,3rd",
            "11,false,",
            "11,true,11th",
            "21,true,21st",
            "23,true,23rd",
            "30,true,30st"
    )
    fun `PodiumUtils position int to podium`(position: Int, podium: Boolean, expectedValue: String) {

        assertEquals(expectedValue, position.position(podium).toString())
    }
}
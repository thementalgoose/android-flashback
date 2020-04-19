package tmg.flashback.repo.models

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.repo.utils.toMaxIfZero

class IntUtilsKtTest {

    @ParameterizedTest
    @CsvSource(
        "1,false",
        "-1,false",
        ",true",
        "0,true"
    )
    fun `wrapping integer to max value if it's null or zero`(value: Int?, expectedToBeIntMax: Boolean) {

        val expectedValue: Int = if (expectedToBeIntMax) Int.MAX_VALUE else value!!

        assertEquals(expectedValue, value.toMaxIfZero())
    }
}
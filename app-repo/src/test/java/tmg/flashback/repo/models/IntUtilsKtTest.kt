package tmg.flashback.repo.models

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.repo.utils.extendTo
import tmg.flashback.repo.utils.toMaxIfZero

class IntUtilsKtTest {

    @ParameterizedTest
    @CsvSource(
        "1,false",
        "-1,false",
        ",true",
        "0,true"
    )
    fun `IntUtils toMaxIfZero wrapping integer to max value if it's null or zero`(value: Int?, expectedToBeIntMax: Boolean) {

        val expectedValue: Int = if (expectedToBeIntMax) Int.MAX_VALUE else value!!

        assertEquals(expectedValue, value.toMaxIfZero())
    }

    @ParameterizedTest
    @CsvSource(
        "0,2,00",
        "1,3,001",
        "02,1,2",
        "213,5,00213"
    )
    fun `IntUtils extendTo extends characters with '0'`(value: Int, extension: Int, expectedValue: String) {

        assertEquals(expectedValue, value.extendTo(extension))
    }
}
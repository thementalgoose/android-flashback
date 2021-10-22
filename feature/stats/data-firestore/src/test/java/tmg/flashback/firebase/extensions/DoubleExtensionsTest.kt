package tmg.flashback.firebase.extensions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class DoubleExtensionsTest {

    @ParameterizedTest(name = "display with {0} input shows {1} as points readout")
    @CsvSource(
        "0.0,0",
        "0.4,0",
        "0.5,0.5",
        "1.00013,1",
        "1.5,1.5",
        "1.50,1.5",
        "1.0,1",
        "6.0,6",
        "213.5,213.5",
        "220,220",
        "0.000002,0",
        "1.50000,1.5",
        "0.499999999999,0.5"
    )
    fun `display method on points double displays the points in a readable format`(input: Double, expected: String) {
        assertEquals(expected, input.pointsDisplay())
    }
}
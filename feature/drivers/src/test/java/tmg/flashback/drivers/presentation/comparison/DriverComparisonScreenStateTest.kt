package tmg.flashback.drivers.presentation.comparison

import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class DriverComparisonScreenStateTest {

    private val fakeComparison = Comparison(
        left = mockk(),
        leftConstructors = emptyList(),
        right = mockk(),
        rightConstructors = emptyList()
    )

    @ParameterizedTest(name = "getPercentage({0},{1}) = ({3}, {4})")
    @CsvSource(
        "4,0,true,1f,0f",
        "0,4,true,0f,1f",
        "4,4,true,1f,1f",
        "2,4,true,0.5f,1f",
        "4,1,true,1f,0.25f",
        "4,0,false,0f,1f",
        "0,4,false,1f,0f",
        "4,4,false,1f,1f",
        "2,4,false,1f,0.5f",
        "4,1,false,0.25f,1f",
    )
    fun `percentage is calculated`(
        a: Double,
        b: Double,
        highIsBest: Boolean,
        expectedLeft: Float,
        expectedRight: Float
    ) {
        assertEquals(expectedLeft to expectedRight, fakeComparison.getPercentages(a.toFloat(), b.toFloat(), highIsBest))
    }
}
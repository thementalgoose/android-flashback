package tmg.flashback.reactiongame

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class LightsOutDelayProviderTest {

    companion object {
        private const val LOWER_BOUND = 500L
        private const val UPPER_BOUND = 2500L
    }

    private lateinit var underTest: LightsOutDelayProvider

    private fun initUnderTest() {
        underTest = LightsOutDelayProvider()
    }

    @ParameterizedTest
    @ValueSource(ints = intArrayOf(
        1,2,3,4,5,6,7,8,9,10,
        11,12,13,14,15,16,17,18,19,20,
        21,22,23,24,25,26,27,28,29,30,
        31,32,33,34,35,36,37,38,39,40,
        41,42,43,44,45,46,47,48,49,50,
    ))
    fun `get delay returns acceptable value`() {
        initUnderTest()

        assertTrue(underTest.getDelay() in LOWER_BOUND..UPPER_BOUND)
    }
}
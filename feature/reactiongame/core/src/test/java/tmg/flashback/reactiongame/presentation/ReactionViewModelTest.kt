package tmg.flashback.reactiongame.presentation

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.device.managers.TimeManager
import tmg.flashback.reactiongame.LightsOutDelayProvider
import tmg.testutils.BaseTest

internal class ReactionViewModelTest: BaseTest() {

    private companion object {
        private const val LIGHTS_OUT_DELAY = 2000L
        private const val TIME_BETWEEN_LIGHTS = 1000L
        private const val TEST_TIMEOUT = 5000L
    }

    private val mockLightsOutDelayProvider: LightsOutDelayProvider = mockk(relaxed = true)
    private val mockTimeManager: TimeManager = mockk(relaxed = true)

    private lateinit var underTest: ReactionViewModel

    private fun initUnderTest() {
        underTest = ReactionViewModel(
            lightsOutDelayProvider = mockLightsOutDelayProvider,
            timeManager = mockTimeManager,
            ioDispatcher = testDispatcher
        )
    }

    @BeforeEach
    fun setUp() {
        every { mockLightsOutDelayProvider.getDelay() } returns LIGHTS_OUT_DELAY
    }

    @Test
    fun `clicking start starts sequence and reacting to it works as expected`() = runTest {
        initUnderTest()
        setNow(0)

        underTest.uiState.test {
            assertEquals(ReactionUiState.Start, awaitItem())

            underTest.start()

            assertEquals(ReactionUiState.Game(lights = 0), awaitItem())
            advanceTimeBy(TIME_BETWEEN_LIGHTS)
            assertEquals(ReactionUiState.Game(lights = 1), awaitItem())
            advanceTimeBy(TIME_BETWEEN_LIGHTS)
            assertEquals(ReactionUiState.Game(lights = 2), awaitItem())
            advanceTimeBy(TIME_BETWEEN_LIGHTS)
            assertEquals(ReactionUiState.Game(lights = 3), awaitItem())
            advanceTimeBy(TIME_BETWEEN_LIGHTS)
            assertEquals(ReactionUiState.Game(lights = 4), awaitItem())
            advanceTimeBy(TIME_BETWEEN_LIGHTS)
            assertEquals(ReactionUiState.Game(lights = 5), awaitItem())

            setNow(10000)

            advanceTimeBy(LIGHTS_OUT_DELAY)
            assertEquals(ReactionUiState.Game(lights = 0, hasDisplayedSequence = true), awaitItem())

            advanceTimeBy(250L)
            setNow(10250)

            underTest.react()

            val expected = ReactionUiState.Results(
                timeMillis = 250,
                tier = ReactionResultTier.AVERAGE
            )
            assertEquals(expected, awaitItem())

            advanceTimeBy(TEST_TIMEOUT)

            expectNoEvents()
        }
    }

    @Test
    fun `reacting to sequence before lights out causes jump start`() = runTest {
        initUnderTest()
        setNow(0)

        underTest.uiState.test {
            assertEquals(ReactionUiState.Start, awaitItem())

            underTest.start()

            advanceTimeBy(TIME_BETWEEN_LIGHTS * 5)
            assertEquals(ReactionUiState.Game(0), awaitItem())
            assertEquals(ReactionUiState.Game(1), awaitItem())
            assertEquals(ReactionUiState.Game(2), awaitItem())
            assertEquals(ReactionUiState.Game(3), awaitItem())
            assertEquals(ReactionUiState.Game(4), awaitItem())
            assertEquals(ReactionUiState.Game(5), awaitItem())

            underTest.react()

            assertEquals(ReactionUiState.JumpStart, awaitItem())
        }
    }

    @ParameterizedTest(name = "reacting to sequence in {0} millis results in {1} tier")
    @CsvSource(
        "10,SUPERHUMAN",
        "150,SUPERHUMAN",
        "160,GREAT",
        "180,GREAT",
        "181,GOOD",
        "230,GOOD",
        "240,AVERAGE",
        "270,AVERAGE",
        "280,NOT_GOOD",
        "320,NOT_GOOD",
        "400,POOR",
        "1000,POOR",
    )
    fun `reacting to sequence after millis results in tier`(millis: Int, expectedTier: ReactionResultTier) = runTest {
        initUnderTest()

        underTest.uiState.test {
            assertEquals(ReactionUiState.Start, awaitItem())

            underTest.start()

            advanceTimeBy(TIME_BETWEEN_LIGHTS * 5)
            assertEquals(ReactionUiState.Game(0), awaitItem())
            assertEquals(ReactionUiState.Game(1), awaitItem())
            assertEquals(ReactionUiState.Game(2), awaitItem())
            assertEquals(ReactionUiState.Game(3), awaitItem())
            assertEquals(ReactionUiState.Game(4), awaitItem())
            assertEquals(ReactionUiState.Game(5), awaitItem())

            setNow(100)
            advanceTimeBy(LIGHTS_OUT_DELAY)
            assertEquals(ReactionUiState.Game(lights = 0, hasDisplayedSequence = true), awaitItem())

            setNow(100 + millis)
            advanceTimeBy(millis.toLong())

            underTest.react()

            val expected = ReactionUiState.Results(
                timeMillis = millis.toLong(),
                tier = expectedTier
            )
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `not reacting to sequence causes missed ui state`() = runTest {
        initUnderTest()

        underTest.uiState.test {
            assertEquals(ReactionUiState.Start, awaitItem())

            underTest.start()

            advanceTimeBy(TIME_BETWEEN_LIGHTS * 5)
            assertEquals(ReactionUiState.Game(0), awaitItem())
            assertEquals(ReactionUiState.Game(1), awaitItem())
            assertEquals(ReactionUiState.Game(2), awaitItem())
            assertEquals(ReactionUiState.Game(3), awaitItem())
            assertEquals(ReactionUiState.Game(4), awaitItem())
            assertEquals(ReactionUiState.Game(5), awaitItem())

            setNow(100)
            advanceTimeBy(LIGHTS_OUT_DELAY)
            assertEquals(ReactionUiState.Game(lights = 0, hasDisplayedSequence = true), awaitItem())

            advanceTimeBy(100L)

            advanceTimeBy(TEST_TIMEOUT)

            val expected = ReactionUiState.Missed
            assertEquals(expected, awaitItem())
        }
    }

    private fun setNow(millis: Int) {
        every { mockTimeManager.nowMillis } returns millis.toLong()
    }
}
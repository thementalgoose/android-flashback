package tmg.flashback.eastereggs.usecases

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.threeten.bp.LocalDateTime
import tmg.flashback.device.managers.TimeManager
import tmg.flashback.device.repository.AccessibilityRepository
import tmg.flashback.eastereggs.repository.EasterEggsRepository

internal class IsSnowEnabledUseCaseTest {

    private val mockAccessibilityRepository: AccessibilityRepository = mockk(relaxed = true)
    private val mockEasterEggsRepository: EasterEggsRepository = mockk(relaxed = true)
    private val mockTimeManager: TimeManager = mockk(relaxed = true)

    private lateinit var underTest: IsSnowEnabledUseCase

    private fun initUnderTest() {
        underTest = IsSnowEnabledUseCase(
            accessibilityRepository = mockAccessibilityRepository,
            easterEggsRepository = mockEasterEggsRepository,
            timeManager = mockTimeManager
        )
    }

    @BeforeEach
    fun setUp() {
        every { mockTimeManager.now } returns LocalDateTime.of(2023, 6, 6, 0, 0)
    }

    @Test
    fun `snow is disabled if animations are disabled`() {
        every { mockAccessibilityRepository.isAnimationsEnabled } returns false

        initUnderTest()
        assertFalse(underTest.invoke())
    }

    @Test
    fun `snow is disabled if config value is disabled`() {
        every { mockAccessibilityRepository.isAnimationsEnabled } returns true
        every { mockEasterEggsRepository.isSnowEnabled } returns false

        initUnderTest()
        assertFalse(underTest.invoke())
    }

    @Test
    fun `snow is enabled if config value is enabled`() {
        every { mockAccessibilityRepository.isAnimationsEnabled } returns true
        every { mockEasterEggsRepository.isSnowEnabled } returns true

        initUnderTest()
        assertTrue(underTest.invoke())
    }

    @ParameterizedTest
    @CsvSource(
        "11-16,false",
        "12-19,false",
        "12-20,true",
        "12-25,true",
        "12-31,true",
        "01-01,true",
        "01-06,true",
        "01-14,true",
        "01-15,false",
        "02-20,false"
    )
    fun `snow is disabled if time is out of range`(
        monthAndDay: String,
        expectedEnabledState: Boolean
    ) {
        val (month, day) = monthAndDay.split("-")[0].toInt() to monthAndDay.split("-")[1].toInt()
        every { mockAccessibilityRepository.isAnimationsEnabled } returns true
        every { mockTimeManager.now } returns LocalDateTime.of(2023, month, day, 12, 10)

        initUnderTest()
        assertEquals(expectedEnabledState, underTest.invoke())
    }

}
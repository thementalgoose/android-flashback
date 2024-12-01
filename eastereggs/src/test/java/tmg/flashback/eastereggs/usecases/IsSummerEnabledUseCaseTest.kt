package tmg.flashback.eastereggs.usecases

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.device.repository.AccessibilityRepository
import tmg.flashback.eastereggs.repository.EasterEggsRepository

internal class IsSummerEnabledUseCaseTest {

    private val mockAccessibilityRepository: AccessibilityRepository = mockk(relaxed = true)
    private val mockEasterEggsRepository: EasterEggsRepository = mockk(relaxed = true)

    private lateinit var underTest: IsSummerEnabledUseCase

    private fun initUnderTest() {
        underTest = IsSummerEnabledUseCase(
            accessibilityRepository = mockAccessibilityRepository,
            easterEggsRepository = mockEasterEggsRepository
        )
    }

    @BeforeEach
    fun setUp() {

    }

    @Test
    fun `summer is disabled if animations are disabled`() {
        every { mockAccessibilityRepository.isAnimationsEnabled } returns false

        initUnderTest()
        assertFalse(underTest.invoke())
    }

    @Test
    fun `summer is disabled if config value is disabled`() {
        every { mockAccessibilityRepository.isAnimationsEnabled } returns true
        every { mockEasterEggsRepository.isSummerEnabled } returns false

        initUnderTest()
        assertFalse(underTest.invoke())
    }

    @Test
    fun `summer is enabled if config value is enabled`() {
        every { mockAccessibilityRepository.isAnimationsEnabled } returns true
        every { mockEasterEggsRepository.isSummerEnabled } returns true

        initUnderTest()
        assertTrue(underTest.invoke())
    }
}
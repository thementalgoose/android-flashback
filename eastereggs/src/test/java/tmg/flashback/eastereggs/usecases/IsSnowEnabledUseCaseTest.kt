package tmg.flashback.eastereggs.usecases

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.flashback.device.repository.AccessibilityRepository
import tmg.flashback.eastereggs.repository.EasterEggsRepository

internal class IsSnowEnabledUseCaseTest {

    private val mockAccessibilityRepository: AccessibilityRepository = mockk(relaxed = true)
    private val mockEasterEggsRepository: EasterEggsRepository = mockk(relaxed = true)

    private lateinit var underTest: IsSnowEnabledUseCase

    private fun initUnderTest() {
        underTest = IsSnowEnabledUseCase(
            accessibilityRepository = mockAccessibilityRepository,
            easterEggsRepository = mockEasterEggsRepository
        )
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

}
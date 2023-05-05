package tmg.flashback.eastereggs.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.manager.ConfigManager

internal class EasterEggsRepositoryTest {

    private val mockConfigManager: ConfigManager = mockk(relaxed = true)

    private lateinit var underTest: EasterEggsRepository

    private fun initUnderTest() {
        underTest = EasterEggsRepository(
            configManager = mockConfigManager
        )
    }

    @Test
    fun `is snow enabled gets from config`() {
        every { mockConfigManager.getBoolean(keySnow) } returns true

        initUnderTest()
        assertTrue(underTest.isSnowEnabled)

        verify {
            mockConfigManager.getBoolean(keySnow)
        }
    }

    @Test
    fun `is ukraine enabled gets from config`() {
        every { mockConfigManager.getBoolean(keyUkraine) } returns true

        initUnderTest()
        assertTrue(underTest.isUkraineEnabled)

        verify {
            mockConfigManager.getBoolean(keyUkraine)
        }
    }

    companion object {
        private const val keySnow = "easteregg_snow"
        private const val keyUkraine = "easteregg_ukraine"
    }
}
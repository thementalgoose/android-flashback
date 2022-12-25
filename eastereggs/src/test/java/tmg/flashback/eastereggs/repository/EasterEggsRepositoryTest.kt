package tmg.flashback.eastereggs.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.eastereggs.model.MenuKeys

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

    companion object {
        private const val keySnow = "easteregg_snow"
    }
}
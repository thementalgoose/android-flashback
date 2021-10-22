package tmg.flashback.ads.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.configuration.manager.ConfigManager

internal class AdsRepositoryTest {

    private val mockConfigManager: ConfigManager = mockk(relaxed = true)

    private lateinit var sut: AdsRepository

    private fun initSUT() {
        sut = AdsRepository(mockConfigManager)
    }

    @Test
    fun `is enabled reads value from configuration manager`() {
        every { mockConfigManager.getBoolean(keyEnabled) } returns true
        initSUT()
        assertTrue(sut.isEnabled)
        verify {
            mockConfigManager.getBoolean(keyEnabled)
        }
    }

    companion object {
        private const val keyEnabled: String = "adverts"
    }
}
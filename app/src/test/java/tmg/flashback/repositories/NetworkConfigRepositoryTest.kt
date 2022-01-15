package tmg.flashback.repositories

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.BuildConfig
import tmg.flashback.configuration.manager.ConfigManager

internal class NetworkConfigRepositoryTest {

    private val mockConfigManager: ConfigManager = mockk(relaxed = true)

    private lateinit var sut: NetworkConfigRepository

    private fun initSUT() {
        sut = NetworkConfigRepository(mockConfigManager)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockConfigManager.getString(keyConfigUrl) } returns "network-base-url"
    }

    @Test
    fun `config url returns value from config manager`() {
        initSUT()
        assertEquals("network-base-url", sut.configUrl)
        verify {
            mockConfigManager.getString(keyConfigUrl)
        }
    }

    @Test
    fun `config url returns default value if config manager is null`() {
        every { mockConfigManager.getString(keyConfigUrl) } returns null
        initSUT()
        assertEquals(BuildConfig.BASE_URL, sut.configUrl)
        verify {
            mockConfigManager.getString(keyConfigUrl)
        }
    }

    companion object {
        private const val keyConfigUrl: String = "config_url"
    }
}
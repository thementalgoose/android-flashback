package tmg.flashback.repositories

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.settings.repository.SettingsRepository

internal class ContactRepositoryTest {

    private val mockConfigManager: ConfigManager = mockk(relaxed = true)

    private lateinit var underTest: ContactRepository

    private fun initUnderTest() {
        underTest = ContactRepository(
            configManager = mockConfigManager
        )
    }

    @Test
    fun `getting contact email returns default if config is null`() {
        initUnderTest()
        assertEquals(fallbackEmail, underTest.contactEmail)
    }

    @Test
    fun `getting contact email returns config value`() {
        val configValue = "config-value"
        every { mockConfigManager.getString(keyEmail) } returns configValue

        initUnderTest()

        assertEquals(configValue, underTest.contactEmail)
    }

    companion object {
        private const val keyEmail: String = "email"

        private const val fallbackEmail: String = "thementalgoose@gmail.com"
    }
}
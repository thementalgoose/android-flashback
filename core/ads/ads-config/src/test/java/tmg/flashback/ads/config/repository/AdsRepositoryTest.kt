package tmg.flashback.ads.config.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.ads.config.repository.json.AdvertConfigJson
import tmg.flashback.ads.config.repository.json.AdvertLocationJson
import tmg.flashback.ads.config.repository.model.AdvertConfig
import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.prefs.manager.PreferenceManager

internal class AdsRepositoryTest {

    private val mockConfigManager: ConfigManager = mockk(relaxed = true)
    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)

    private lateinit var sut: AdsRepository

    private fun initSUT() {
        sut = AdsRepository(mockConfigManager, mockPreferenceManager)
    }

    @Test
    fun `advert user pref reads from preferences`() {
        every { mockPreferenceManager.getBoolean(keyUserPreferences, true) } returns true
        initSUT()
        assertTrue(sut.userPrefEnabled)
        verify {
            mockPreferenceManager.getBoolean(keyUserPreferences, true)
        }
    }

    @Test
    fun `advert user pref writes to preferences`() {
        initSUT()
        sut.userPrefEnabled = true
        verify {
            mockPreferenceManager.save(keyUserPreferences, true)
        }
    }

    @Test
    fun `advert config reads value from config manager`() {
        val input = AdvertConfigJson(
            locations = AdvertLocationJson(
                home = true,
                race = true,
                search = true,
                rss = true
            ),
            allowUserConfig = true
        )
        val expected = AdvertConfig(
            onHomeScreen = true,
            onRaceScreen = true,
            onSearch = true,
            onRss = true,
            allowUserConfig = true
        )
        every { mockConfigManager.getJson(keyAdverts, AdvertConfigJson.serializer()) } returns input
        initSUT()

        assertEquals(sut.advertConfig, expected)
        assertTrue(sut.isEnabled)
        assertTrue(sut.allowUserConfig)

        verify {
            mockConfigManager.getJson(keyAdverts, AdvertConfigJson.serializer())
        }
    }

    @Test
    fun `advert config with false values json returns all false model`() {
        val input = AdvertConfigJson(
            locations = AdvertLocationJson(
                home = false,
                race = false,
                search = false,
                rss = false
            ),
            allowUserConfig = false
        )
        val expected = AdvertConfig(
            onHomeScreen = false,
            onRaceScreen = false,
            onSearch = false,
            onRss = false,
            allowUserConfig = false
        )
        every { mockConfigManager.getJson(keyAdverts, AdvertConfigJson.serializer()) } returns input
        initSUT()

        assertEquals(sut.advertConfig, expected)
        assertFalse(sut.isEnabled)
        assertFalse(sut.allowUserConfig)

        verify {
            mockConfigManager.getJson(keyAdverts, AdvertConfigJson.serializer())
        }
    }

    //region Are Adverts Enabled

    @Test
    fun `are adverts enabled returns user pref if user config is allowed`() {
        mockConfig(isEnabled = true, allowUserConfig = false)
        every { mockPreferenceManager.getBoolean(keyUserPreferences, true) } returns true
        initSUT()
        assertTrue(sut.areAdvertsEnabled)
    }

    @Test
    fun `are adverts enabled reads value from repository`() {
        mockConfig(isEnabled = true, allowUserConfig = false)
        initSUT()
        assertTrue(sut.areAdvertsEnabled)
    }

    @Test
    fun `are adverts enabled changes if user pref value does`() {
        mockConfig(isEnabled = true, allowUserConfig = true)
        every { mockPreferenceManager.getBoolean(keyUserPreferences, true) } returns true
        initSUT()
        assertTrue(sut.areAdvertsEnabled)

        every { mockPreferenceManager.getBoolean(keyUserPreferences, true) } returns false
        assertFalse(sut.areAdvertsEnabled)
    }

    //endregion

    private fun mockConfig(isEnabled: Boolean = true, allowUserConfig: Boolean = false) {
        val input = AdvertConfigJson(
            locations = AdvertLocationJson(
                home = isEnabled,
                race = false,
                search = false,
                rss = false
            ),
            allowUserConfig = allowUserConfig
        )
        every { mockConfigManager.getJson(keyAdverts, AdvertConfigJson.serializer()) } returns input
    }

    companion object {
        private const val keyAdverts: String = "advert_config"
        private const val keyUserPreferences: String = "ADVERT_PREF"
    }
}
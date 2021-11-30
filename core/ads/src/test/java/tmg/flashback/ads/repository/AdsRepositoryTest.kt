package tmg.flashback.ads.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.ads.repository.json.AdvertConfigJson
import tmg.flashback.ads.repository.json.AdvertLocationJson
import tmg.flashback.ads.repository.model.AdvertConfig
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
                driverOverview = true,
                constructorOverview = true,
                search = true,
                rss = true
            ),
            allowUserConfig = true
        )
        val expected = AdvertConfig(
            onHomeScreen = true,
            onRaceScreen = true,
            onDriverOverview = true,
            onConstructorOverview = true,
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
                driverOverview = false,
                constructorOverview = false,
                search = false,
                rss = false
            ),
            allowUserConfig = false
        )
        val expected = AdvertConfig(
            onHomeScreen = false,
            onRaceScreen = false,
            onDriverOverview = false,
            onConstructorOverview = false,
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

    companion object {
        private const val keyAdverts: String = "adverts"
        private const val keyUserPreferences: String = "ADVERT_PREF"
    }
}
package tmg.configuration.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.configuration.managers.RemoteConfigManager
import tmg.configuration.repository.models.ForceUpgrade
import tmg.core.device.repository.SharedPreferenceRepository
import java.time.Year

internal class ConfigRepositoryTest {

    private val mockRemoteConfigManager: RemoteConfigManager = mockk(relaxed = true)
    private val mockSharedPreferenceRepository: SharedPreferenceRepository = mockk(relaxed = true)

    private lateinit var sut: ConfigRepository

    private fun initSUT() {
        sut = ConfigRepository(mockRemoteConfigManager, mockSharedPreferenceRepository)
    }

    //region Remote Config Sync count

    @Test
    fun `remote config sync count calls shared prefs repository`() {
        every { mockSharedPreferenceRepository.getInt(keyRemoteConfigSync, any()) } returns 3
        initSUT()
        assertEquals(sut.remoteConfigSync, 3)
        verify {
            mockSharedPreferenceRepository.getInt(keyRemoteConfigSync, 0)
        }
    }

    @Test
    fun `remote config sync count saves in shared prefs repository`() {
        initSUT()
        sut.remoteConfigSync = 2
        verify {
            mockSharedPreferenceRepository.save(keyRemoteConfigSync, 2)
        }
    }

    //endregion

    //region Supported Seasons

    @Test
    fun `supported seasons calls remote config`() {
        val expected = setOf(2020, 2019)
        every { mockRemoteConfigManager.getString(keySupportedSeasons) } returns
                """
        {
            "seasons": [
                {
                    "s": 2019
                },
                {
                    "s": 2020
                }
            ]
        }
        """.trimIndent()

        initSUT()
        assertEquals(sut.supportedSeasons, expected)
    }

    @Test
    fun `supported seasons with invalid json returns empty set`() {
        every { mockRemoteConfigManager.getString(keySupportedSeasons) } returns """
        {
            "somethingelse": true
        }
        """.trimIndent()

        initSUT()
        assertEquals(sut.supportedSeasons, emptySet<Int>())
    }

    //endregion

    //region Default season

    @Test
    fun `default season calls remote config`() {
        every { mockRemoteConfigManager.getString(keyDefaultYear) } returns "2020"
        initSUT()
        assertEquals(2020, sut.defaultSeason)
        verify {
            mockRemoteConfigManager.getString(keyDefaultYear)
        }
    }

    @Test
    fun `default season value doesnt update if called again`() {
        every { mockRemoteConfigManager.getString(keyDefaultYear) } returns "2020"
        initSUT()

        assertEquals(2020, sut.defaultSeason)
        verify {
            mockRemoteConfigManager.getString(keyDefaultYear)
        }

        every { mockRemoteConfigManager.getString(keyDefaultYear) } returns "2019"
        assertEquals(2020, sut.defaultSeason)
        verify(exactly = 0) {
            mockRemoteConfigManager.getString(keyDefaultYear)
        }
    }

    @Test
    fun `default season when value is empty return current year`() {
        every { mockRemoteConfigManager.getString(keyDefaultYear) } returns "not-an-int"
        initSUT()
        assertEquals(Year.now().value, sut.defaultSeason)
        verify {
            mockRemoteConfigManager.getString(keyDefaultYear)
        }
    }

    //endregion

    //region Banner

    @Test
    fun `banner calls remote config`() {
        every { mockRemoteConfigManager.getString(keyDefaultBanner) } returns "test"
        initSUT()
        assertEquals("test", sut.banner)
        verify {
            mockRemoteConfigManager.getString(keyDefaultBanner)
        }
    }

    //endregion

    //region Force upgrade

    @Test
    fun `force upgrade calls remote config`() {

        val expected = ForceUpgrade(
            title = "hello",
            message = "howdy",
            link = Pair("test", "http://www.google.com")
        )

        every { mockRemoteConfigManager.getString(keyForceUpgrade) } returns """
        {
           "title": "hello",
           "message": "howdy",
           "linkText": "test",
           "link": "http://www.google.com"
        }
        """.trimIndent()

        initSUT()
        assertEquals(expected, sut.forceUpgrade)

        verify {
            mockRemoteConfigManager.getString(keyForceUpgrade)
        }
    }

    @Test
    fun `force upgrade with no link calls remote config`() {

        val expected = ForceUpgrade(
            title = "hello",
            message = "howdy",
            link = null
        )

        every { mockRemoteConfigManager.getString(keyForceUpgrade) } returns """
        {
           "title": "hello",
           "message": "howdy"
        }
        """.trimIndent()

        initSUT()
        assertEquals(expected, sut.forceUpgrade)
    }

    @Test
    fun `force upgrade title is empty`() {
        every { mockRemoteConfigManager.getString(keyForceUpgrade) } returns """
        {
           "title": null,
           "message": "howdy"
        }
        """.trimIndent()

        initSUT()
        assertNull(sut.forceUpgrade)
    }

    @Test
    fun `force upgrade message is empty`() {
        every { mockRemoteConfigManager.getString(keyForceUpgrade) } returns """
        {
           "title": "test",
           "message": null
        }
        """.trimIndent()

        initSUT()
        assertNull(sut.forceUpgrade)
    }

    //endregion

    //region Data provided

    @Test
    fun `data provided by calls remote config`() {
        every { mockRemoteConfigManager.getString(keyDataProvidedBy) } returns "test"
        initSUT()
        assertEquals("test", sut.dataProvidedBy)
        verify {
            mockRemoteConfigManager.getString(keyDataProvidedBy)
        }
    }

    //endregion

    //region Dashboard Calendar

    @Test
    fun `dashboard calendar calls remote config`() {
        every { mockRemoteConfigManager.getBoolean(keyDashboardCalendar) } returns true
        initSUT()
        assertTrue(sut.dashboardCalendar)
        verify {
            mockRemoteConfigManager.getBoolean(keyDashboardCalendar)
        }
    }

    @Test
    fun `dashboard calendar value doesnt change when called again`() {
        every { mockRemoteConfigManager.getBoolean(keyDashboardCalendar) } returns true
        initSUT()

        assertTrue(sut.dashboardCalendar)
        verify {
            mockRemoteConfigManager.getBoolean(keyDashboardCalendar)
        }

        every { mockRemoteConfigManager.getBoolean(keyDashboardCalendar) } returns false
        assertTrue(sut.dashboardCalendar)
        verify(exactly = 0) {
            mockRemoteConfigManager.getBoolean(keyDashboardCalendar)
        }
    }

    //endregion

    //region Up Next

    @Test
    fun `up next`() {
        TODO("Implement me!")
    }

    //endregion

    //region RSS

    @Test
    fun `rss calls remote config`() {
        every { mockRemoteConfigManager.getBoolean(keyRss) } returns true
        initSUT()
        assertTrue(sut.rss)
        verify {
            mockRemoteConfigManager.getBoolean(keyRss)
        }
    }

    @Test
    fun `rss value doesnt change when called again`() {
        every { mockRemoteConfigManager.getBoolean(keyRss) } returns true
        initSUT()

        assertTrue(sut.rss)
        verify {
            mockRemoteConfigManager.getBoolean(keyRss)
        }

        every { mockRemoteConfigManager.getBoolean(keyRss) } returns false
        assertTrue(sut.rss)
        verify(exactly = 0) {
            mockRemoteConfigManager.getBoolean(keyRss)
        }
    }

    //endregion

    //region RSS Add Custom

    @Test
    fun `rss add custom calls remote config`() {
        every { mockRemoteConfigManager.getBoolean(keyRss) } returns true
        initSUT()
        assertTrue(sut.rssAddCustom)
        verify {
            mockRemoteConfigManager.getBoolean(keyRss)
        }
    }

    @Test
    fun `rss add custom value doesnt change when called again`() {
        every { mockRemoteConfigManager.getBoolean(keyRssAddCustom) } returns true
        initSUT()

        assertTrue(sut.rssAddCustom)
        verify {
            mockRemoteConfigManager.getBoolean(keyRssAddCustom)
        }

        every { mockRemoteConfigManager.getBoolean(keyRssAddCustom) } returns false
        assertTrue(sut.rssAddCustom)
        verify(exactly = 0) {
            mockRemoteConfigManager.getBoolean(keyRssAddCustom)
        }
    }

    //endregion

    //region RSS Supported Sources

    @Test
    fun `rss supported seasons`() {
        TODO("Implement me!")
    }

    //endregion

    companion object {
        private const val keyRemoteConfigSync: String = "REMOTE_CONFIG_SYNC_COUNT"

        private const val keyDefaultYear: String = "default_year"
        private const val keyUpNext: String = "up_next"
        private const val keyDefaultBanner: String = "banner"
        private const val keyForceUpgrade: String = "force_upgrade"
        private const val keyDataProvidedBy: String = "data_provided"
        private const val keySupportedSeasons: String = "supported_seasons"
        private const val keyDashboardCalendar: String = "dashboard_calendar"
        private const val keyRss: String = "rss"
        private const val keyRssAddCustom: String = "rss_add_custom"
        private const val keyRssSupportedSources: String = "rss_supported_sources"
    }
}
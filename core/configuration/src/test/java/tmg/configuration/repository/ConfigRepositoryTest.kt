package tmg.configuration.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.configuration.services.RemoteConfigService
import tmg.configuration.repository.models.ForceUpgrade
import tmg.core.prefs.manager.PreferenceManager
import java.time.Year

internal class ConfigRepositoryTest {

    private val mockRemoteConfigService: RemoteConfigService = mockk(relaxed = true)
    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)

    private lateinit var sut: ConfigRepository

    private fun initSUT() {
        sut = ConfigRepository(mockRemoteConfigService, mockPreferenceManager)
    }

    //region Remote Config Sync count

    @Test
    fun `remote config sync count calls shared prefs repository`() {
        every { mockPreferenceManager.getInt(keyRemoteConfigSync, any()) } returns 3
        initSUT()
        assertEquals(sut.remoteConfigSync, 3)
        verify {
            mockPreferenceManager.getInt(keyRemoteConfigSync, 0)
        }
    }

    @Test
    fun `remote config sync count saves in shared prefs repository`() {
        initSUT()
        sut.remoteConfigSync = 2
        verify {
            mockPreferenceManager.save(keyRemoteConfigSync, 2)
        }
    }

    //endregion

    //region Supported Seasons

    @Test
    fun `supported seasons calls remote config`() {
        val expected = setOf(2020, 2019)
        every { mockRemoteConfigService.getString(keySupportedSeasons) } returns
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
        every { mockRemoteConfigService.getString(keySupportedSeasons) } returns """
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
        every { mockRemoteConfigService.getString(keyDefaultYear) } returns "2020"
        initSUT()
        assertEquals(2020, sut.defaultSeason)
        verify {
            mockRemoteConfigService.getString(keyDefaultYear)
        }
    }

    @Test
    fun `default season value doesnt update if called again`() {
        every { mockRemoteConfigService.getString(keyDefaultYear) } returns "2020"
        initSUT()

        assertEquals(2020, sut.defaultSeason)
        verify {
            mockRemoteConfigService.getString(keyDefaultYear)
        }

        every { mockRemoteConfigService.getString(keyDefaultYear) } returns "2019"
        assertEquals(2020, sut.defaultSeason)
        verify(exactly = 0) {
            mockRemoteConfigService.getString(keyDefaultYear)
        }
    }

    @Test
    fun `default season when value is empty return current year`() {
        every { mockRemoteConfigService.getString(keyDefaultYear) } returns "not-an-int"
        initSUT()
        assertEquals(Year.now().value, sut.defaultSeason)
        verify {
            mockRemoteConfigService.getString(keyDefaultYear)
        }
    }

    //endregion

    //region Banner

    @Test
    fun `banner calls remote config`() {
        every { mockRemoteConfigService.getString(keyDefaultBanner) } returns "test"
        initSUT()
        assertEquals("test", sut.banner)
        verify {
            mockRemoteConfigService.getString(keyDefaultBanner)
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

        every { mockRemoteConfigService.getString(keyForceUpgrade) } returns """
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
            mockRemoteConfigService.getString(keyForceUpgrade)
        }
    }

    @Test
    fun `force upgrade with no link calls remote config`() {

        val expected = ForceUpgrade(
            title = "hello",
            message = "howdy",
            link = null
        )

        every { mockRemoteConfigService.getString(keyForceUpgrade) } returns """
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
        every { mockRemoteConfigService.getString(keyForceUpgrade) } returns """
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
        every { mockRemoteConfigService.getString(keyForceUpgrade) } returns """
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
        every { mockRemoteConfigService.getString(keyDataProvidedBy) } returns "test"
        initSUT()
        assertEquals("test", sut.dataProvidedBy)
        verify {
            mockRemoteConfigService.getString(keyDataProvidedBy)
        }
    }

    //endregion

    //region Dashboard Calendar

    @Test
    fun `dashboard calendar calls remote config`() {
        every { mockRemoteConfigService.getBoolean(keyDashboardCalendar) } returns true
        initSUT()
        assertTrue(sut.dashboardCalendar)
        verify {
            mockRemoteConfigService.getBoolean(keyDashboardCalendar)
        }
    }

    @Test
    fun `dashboard calendar value doesnt change when called again`() {
        every { mockRemoteConfigService.getBoolean(keyDashboardCalendar) } returns true
        initSUT()

        assertTrue(sut.dashboardCalendar)
        verify {
            mockRemoteConfigService.getBoolean(keyDashboardCalendar)
        }

        every { mockRemoteConfigService.getBoolean(keyDashboardCalendar) } returns false
        assertTrue(sut.dashboardCalendar)
        verify(exactly = 1) {
            mockRemoteConfigService.getBoolean(keyDashboardCalendar)
        }
    }

    //endregion

    companion object {
        private const val keyRemoteConfigSync: String = "REMOTE_CONFIG_SYNC_COUNT"

        private const val keyDefaultYear: String = "default_year"
        private const val keyDefaultBanner: String = "banner"
        private const val keyForceUpgrade: String = "force_upgrade"
        private const val keyDataProvidedBy: String = "data_provided"
        private const val keySupportedSeasons: String = "supported_seasons"
        private const val keyDashboardCalendar: String = "dashboard_calendar"
    }
}
package tmg.configuration.controllers

import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.configuration.constants.Migrations
import tmg.configuration.services.RemoteConfigService
import tmg.configuration.repository.ConfigRepository
import tmg.configuration.repository.models.ForceUpgrade
import tmg.configuration.repository.models.SupportedSource
import tmg.configuration.repository.models.UpNextSchedule

internal class ConfigControllerTest {

    private val mockConfigService: RemoteConfigService = mockk(relaxed = true)
    private val mockConfigRepository: ConfigRepository = mockk(relaxed = true)

    private lateinit var sut: ConfigController

    private fun initSUT() {
        sut = ConfigController(mockConfigRepository, mockConfigService)
    }

    @Test
    fun `on init set defaults is called`() {

        initSUT()

        verify {
            mockConfigService.initialiseRemoteConfig()
        }
    }

    //region Require sync

    @Test
    fun `require synchronisation reads value from remote config sync`() {

        every { mockConfigRepository.remoteConfigSync } returns 0
        initSUT()

        assertTrue(sut.requireSynchronisation)
        verify {
            mockConfigRepository.remoteConfigSync
        }
    }

    @Test
    fun `require synchronisation returns false when migrations match`() {

        every { mockConfigRepository.remoteConfigSync } returns Migrations.configurationSyncCount
        initSUT()

        assertFalse(sut.requireSynchronisation)
        verify {
            mockConfigRepository.remoteConfigSync
        }
    }

    //endregion

    //region Fetching / updating logic

    @Test
    fun `fetch calls update in manager`() {
        initSUT()
        runBlockingTest {
            sut.fetch()
        }

        coVerify {
            mockConfigService.fetch(false)
        }
    }

    @Test
    fun `fetch and apply calls update in manager and saves remote config sync`() {
        coEvery { mockConfigService.fetch(true) } returns true
        initSUT()
        runBlockingTest {
            sut.fetchAndApply()
        }

        coVerify {
            mockConfigService.fetch(true)
        }
        verify {
            mockConfigRepository.remoteConfigSync = Migrations.configurationSyncCount
        }
    }

    @Test
    fun `apply pending calls manager`() {
        initSUT()
        runBlockingTest {
            sut.applyPending()
        }

        coVerify {
            mockConfigService.activate()
        }
    }

    //endregion

    //region Variables

    @Test
    fun `supported seasons calls config repo`() {
        every { mockConfigRepository.supportedSeasons } returns setOf(2012)
        initSUT()
        assertEquals(setOf(2012), sut.supportedSeasons)
        verify {
            mockConfigRepository.supportedSeasons
        }
    }

    @Test
    fun `default season calls config repo`() {
        every { mockConfigRepository.defaultSeason } returns 2019
        initSUT()
        assertEquals(2019, sut.defaultSeason)
        verify {
            mockConfigRepository.defaultSeason
        }
    }

    @Test
    fun `banner calls config repo`() {
        every { mockConfigRepository.banner } returns "banner-time"
        initSUT()
        assertEquals("banner-time", sut.banner)
        verify {
            mockConfigRepository.banner
        }
    }

    @Test
    fun `force upgrade calls config repo`() {
        val expected = ForceUpgrade(title = "test", message = "hello", link = null)
        every { mockConfigRepository.forceUpgrade } returns expected
        initSUT()
        assertEquals(expected, sut.forceUpgrade)
        verify {
            mockConfigRepository.forceUpgrade
        }
    }

    @Test
    fun `dashboard calendar calls config repo`() {
        every { mockConfigRepository.dashboardCalendar } returns true
        initSUT()
        assertTrue(sut.dashboardCalendar)
        verify {
            mockConfigRepository.dashboardCalendar
        }
    }

    @Test
    fun `data provided by calls config repo`() {
        every { mockConfigRepository.dataProvidedBy } returns "banner-time"
        initSUT()
        assertEquals("banner-time", sut.dataProvidedBy)
        verify {
            mockConfigRepository.dataProvidedBy
        }
    }

    @Test
    fun `up next calls config repo`() {
        val expected = UpNextSchedule(1, 1, "", null, emptyList(), "PRT", "algarve")
        every { mockConfigRepository.upNext } returns listOf(expected)
        initSUT()
        assertEquals(listOf(expected), sut.upNext)
        verify {
            mockConfigRepository.upNext
        }
    }

    @Test
    fun `rss calls config repo`() {
        every { mockConfigRepository.rss } returns true
        initSUT()
        assertTrue(sut.rss)
        verify {
            mockConfigRepository.rss
        }
    }

    @Test
    fun `rss add custom calls config repo`() {
        every { mockConfigRepository.rssAddCustom } returns true
        initSUT()
        assertTrue(sut.rssAddCustom)
        verify {
            mockConfigRepository.rssAddCustom
        }
    }

    @Test
    fun `rss supported sources calls config repo`() {
        val expected = SupportedSource("", "", "", "", "", "", "")
        every { mockConfigRepository.rssSupportedSources } returns listOf(expected)
        initSUT()
        assertEquals(listOf(expected), sut.rssSupportedSources)
        verify {
            mockConfigRepository.rssSupportedSources
        }
    }

    //endregion
}
package tmg.flashback.core.controllers

import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.core.constants.Migrations
import tmg.flashback.core.managers.ConfigurationManager
import tmg.flashback.core.model.ForceUpgrade
import tmg.flashback.core.model.SupportedSource
import tmg.flashback.core.model.Timestamp
import tmg.flashback.core.model.UpNextSchedule
import tmg.flashback.core.repositories.ConfigurationRepository
import tmg.flashback.core.repositories.CoreRepository

internal class ConfigurationControllerTest {

    private val mockConfigurationRepository: ConfigurationRepository = mockk(relaxed = true)
    private val mockCoreRepository: CoreRepository = mockk(relaxed = true)
    private val mockConfigurationManager: ConfigurationManager = mockk(relaxed = true)

    private lateinit var sut: ConfigurationController

    private fun initSUT() {
        sut = ConfigurationController(mockConfigurationRepository, mockCoreRepository, mockConfigurationManager)
    }

    @Test
    fun `on init set defaults is called`() {

        initSUT()

        verify {
            mockConfigurationManager.setDefaults()
        }
    }

    //region Require sync

    @Test
    fun `require synchronisation reads value from remote config sync`() {

        every { mockCoreRepository.remoteConfigSync } returns 0
        initSUT()

        assertTrue(sut.requireSynchronisation)
        verify {
            mockCoreRepository.remoteConfigSync
        }
    }

    @Test
    fun `require synchronisation returns false when migrations match`() {

        every { mockCoreRepository.remoteConfigSync } returns Migrations.configurationSyncCount
        initSUT()

        assertFalse(sut.requireSynchronisation)
        verify {
            mockCoreRepository.remoteConfigSync
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
            mockConfigurationManager.update(false)
        }
    }

    @Test
    fun `fetch and apply calls update in manager and saves remote config sync`() {
        coEvery { mockConfigurationManager.update(true) } returns true
        initSUT()
        runBlockingTest {
            sut.fetchAndApply()
        }

        coVerify {
            mockConfigurationManager.update(true)
        }
        verify {
            mockCoreRepository.remoteConfigSync = Migrations.configurationSyncCount
        }
    }

    @Test
    fun `apply pending calls manager`() {
        initSUT()
        runBlockingTest {
            sut.applyPending()
        }

        coVerify {
            mockConfigurationManager.activate()
        }
    }

    //endregion

    //region Variables

    @Test
    fun `supported seasons returns non cached value`() {

        val input1 = setOf(2018)
        val input2 = setOf(2017)

        initSUT()
        every { mockConfigurationRepository.supportedSeasons } returns input1
        assertEquals(sut.supportedSeasons, input1)
        every { mockConfigurationRepository.supportedSeasons } returns input2
        assertEquals(sut.supportedSeasons, input2)
    }

    @Test
    fun `default season returns cached value`() {

        val input1 = 2018
        val input2 = 2017

        initSUT()
        every { mockConfigurationRepository.defaultSeason } returns input1
        assertEquals(sut.defaultSeason, input1)
        every { mockConfigurationRepository.defaultSeason } returns input2
        assertEquals(sut.defaultSeason, input1)
    }

    @Test
    fun `up next list returns non cached value`() {

        val input1 = listOf(UpNextSchedule(1,0,"", Timestamp(LocalDate.now()),null,null,null))
        val input2 = listOf(UpNextSchedule(2,0,"", Timestamp(LocalDate.now()),null,null,null))

        initSUT()
        every { mockConfigurationRepository.upNext } returns input1
        assertEquals(sut.upNext, input1)
        every { mockConfigurationRepository.upNext } returns input2
        assertEquals(sut.upNext, input2)
    }

    @Test
    fun `banner returns non cached value`() {

        val input1 = "banner value 1"
        val input2 = "banner value 2"

        initSUT()
        every { mockConfigurationRepository.banner } returns input1
        assertEquals(sut.banner, input1)
        every { mockConfigurationRepository.banner } returns input2
        assertEquals(sut.banner, input2)
    }

    @Test
    fun `force upgrade returns cached value`() {

        val input1 = ForceUpgrade("title1", "message", Pair("link", "https://www.google.com"))
        val input2 = ForceUpgrade("title2", "message", Pair("link", "https://www.google.com"))

        initSUT()
        every { mockConfigurationRepository.forceUpgrade } returns input1
        assertEquals(sut.forceUpgrade, input1)
        every { mockConfigurationRepository.forceUpgrade } returns input2
        assertEquals(sut.forceUpgrade, input1)
    }

    @Test
    fun `data provided by returns non cached value`() {

        val input1 = "welp"
        val input2 = "second"

        initSUT()
        every { mockConfigurationRepository.dataProvidedBy } returns input1
        assertEquals(sut.dataProvidedBy, input1)
        every { mockConfigurationRepository.dataProvidedBy } returns input2
        assertEquals(sut.dataProvidedBy, input2)
    }

    @Test
    fun `search returns cached value`() {

        val input1 = true
        val input2 = false

        initSUT()
        every { mockConfigurationRepository.search } returns input1
        assertEquals(sut.search, input1)
        every { mockConfigurationRepository.search } returns input2
        assertEquals(sut.search, input1)
    }

    //endregion

    //region Variables - RSS

    @Test
    fun `rss returns cached value`() {

        val input1 = true
        val input2 = false

        initSUT()
        every { mockConfigurationRepository.rss } returns input1
        assertEquals(sut.rss, input1)
        every { mockConfigurationRepository.rss } returns input2
        assertEquals(sut.rss, input1)
    }

    @Test
    fun `rss add custom returns cached value`() {

        val input1 = true
        val input2 = false

        initSUT()
        every { mockConfigurationRepository.rssAddCustom } returns input1
        assertEquals(sut.rssAddCustom, input1)
        every { mockConfigurationRepository.rssAddCustom } returns input2
        assertEquals(sut.rssAddCustom, input1)
    }


    @Test
    fun `rss supported sources returns cached value`() {

        val input1 = listOf(SupportedSource("AGAIN", "", "", "", "", "", ""))
        val input2 = listOf(SupportedSource("INFO", "", "", "", "", "", ""))

        initSUT()
        every { mockConfigurationRepository.rssSupportedSources } returns input1
        assertEquals(sut.rssSupportedSources, input1)
        every { mockConfigurationRepository.rssSupportedSources } returns input2
        assertEquals(sut.rssSupportedSources, input1)
    }

    //endregion
}
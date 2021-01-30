package tmg.flashback.core.controllers

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.core.managers.ConfigurationManager
import tmg.flashback.core.model.SupportedArticleSource
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
    fun `ConfigurationController`() {
        TODO("Tests for requires sync, update, activate, etc.")
    }

    @Test
    fun `ConfigurationController supported seasons returns non cached value`() {

        val input1 = setOf(2018)
        val input2 = setOf(2017)

        initSUT()
        every { mockConfigurationRepository.supportedSeasons } returns input1
        assertEquals(sut.supportedSeasons, input1)
        every { mockConfigurationRepository.supportedSeasons } returns input2
        assertEquals(sut.supportedSeasons, input2)
    }

    @Test
    fun `ConfigurationController default season returns cached value`() {

        val input1 = 2018
        val input2 = 2017

        initSUT()
        every { mockConfigurationRepository.defaultSeason } returns input1
        assertEquals(sut.defaultSeason, input1)
        every { mockConfigurationRepository.defaultSeason } returns input2
        assertEquals(sut.defaultSeason, input1)
    }

    @Test
    fun `ConfigurationController up next list returns non cached value`() {

        val input1 = listOf(UpNextSchedule(1,0,"", Timestamp(LocalDate.now()),null,null,null))
        val input2 = listOf(UpNextSchedule(2,0,"", Timestamp(LocalDate.now()),null,null,null))

        initSUT()
        every { mockConfigurationRepository.upNext } returns input1
        assertEquals(sut.upNext, input1)
        every { mockConfigurationRepository.upNext } returns input2
        assertEquals(sut.upNext, input2)
    }

    @Test
    fun `ConfigurationController banner returns non cached value`() {

        val input1 = "banner value 1"
        val input2 = "banner value 2"

        initSUT()
        every { mockConfigurationRepository.banner } returns input1
        assertEquals(sut.banner, input1)
        every { mockConfigurationRepository.banner } returns input2
        assertEquals(sut.banner, input2)
    }

    @Test
    fun `ConfigurationController data provided by returns non cached value`() {

        val input1 = "welp"
        val input2 = "second"

        initSUT()
        every { mockConfigurationRepository.dataProvidedBy } returns input1
        assertEquals(sut.dataProvidedBy, input1)
        every { mockConfigurationRepository.dataProvidedBy } returns input2
        assertEquals(sut.dataProvidedBy, input2)
    }

    @Test
    fun `ConfigurationController search returns cached value`() {

        val input1 = true
        val input2 = false

        initSUT()
        every { mockConfigurationRepository.search } returns input1
        assertEquals(sut.search, input1)
        every { mockConfigurationRepository.search } returns input2
        assertEquals(sut.search, input1)
    }

    //region RSS

    @Test
    fun `ConfigurationController rss returns cached value`() {

        val input1 = true
        val input2 = false

        initSUT()
        every { mockConfigurationRepository.rss } returns input1
        assertEquals(sut.rss, input1)
        every { mockConfigurationRepository.rss } returns input2
        assertEquals(sut.rss, input1)
    }

    @Test
    fun `ConfigurationController rss add custom returns cached value`() {

        val input1 = true
        val input2 = false

        initSUT()
        every { mockConfigurationRepository.rssAddCustom } returns input1
        assertEquals(sut.rssAddCustom, input1)
        every { mockConfigurationRepository.rssAddCustom } returns input2
        assertEquals(sut.rssAddCustom, input1)
    }


    @Test
    fun `ConfigurationController rss supported sources returns cached value`() {

        val input1 = listOf(SupportedArticleSource("AGAIN", "", "", "", "", "", ""))
        val input2 = listOf(SupportedArticleSource("INFO", "", "", "", "", "", ""))

        initSUT()
        every { mockConfigurationRepository.rssSupportedSources } returns input1
        assertEquals(sut.rssSupportedSources, input1)
        every { mockConfigurationRepository.rssSupportedSources } returns input2
        assertEquals(sut.rssSupportedSources, input1)
    }

    //endregion
}
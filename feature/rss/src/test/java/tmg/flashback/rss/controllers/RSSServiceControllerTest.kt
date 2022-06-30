package tmg.flashback.rss.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.appshortcuts.manager.AppShortcutManager
import tmg.flashback.appshortcuts.models.ShortcutInfo
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.rss.repo.model.SupportedSource
import tmg.testutils.BaseTest

internal class RSSServiceControllerTest: BaseTest() {

    private val mockRssRepository: RSSRepository = mockk(relaxed = true)
    private val mockAppShortcutManager: AppShortcutManager = mockk(relaxed = true)

    private lateinit var underTest: RSSController

    private fun initSUT() {
        underTest = RSSController(mockRssRepository, mockAppShortcutManager)
    }

    @Test
    fun `add app shortcuts adds shortcut to manager`() {

        val slot = slot<ShortcutInfo>()

        initSUT()
        underTest.addAppShortcut()
        verify {
            mockAppShortcutManager.addDynamicShortcut(capture(slot))
        }
        assertEquals("rss", slot.captured.id)
    }

    @Test
    fun `remove app shortcuts removes shortcut to manager`() {
        initSUT()
        underTest.removeAppShortcut()
        verify {
            mockAppShortcutManager.removeDynamicShortcut("rss")
        }
    }

    @Test
    fun `supported sources maps values from configuration controller`() {

        val expected = SupportedArticleSource("https://www.google.com", "", "", "", "", "", "")

        every { mockRssRepository.supportedSources } returns listOf(
            SupportedSource("https://www.google.com", "", "", "", "", "", "")
        )
        initSUT()

        assertEquals(listOf(expected), underTest.sources)
    }

    @Test
    fun `supported sources returns empty when nothing in configuration manager`() {
        every { mockRssRepository.supportedSources } returns emptyList()
        initSUT()

        assertEquals(emptyList<SupportedArticleSource>(), underTest.sources)
    }

    @Test
    fun `supported sources value doesnt change if configuration changes`() {
        every { mockRssRepository.supportedSources } returns listOf(
            SupportedSource("https://www.google1.com", "", "", "", "", "", "")
        )
        initSUT()

        assertEquals(1, underTest.sources.size)
        every { mockRssRepository.supportedSources } returns emptyList()
        assertEquals(1, underTest.sources.size)
    }

    @Test
    fun `add custom feeds returns custom feed value from configuration manager`() {
        every { mockRssRepository.addCustom } returns true
        initSUT()

        assertTrue(underTest.showAddCustomFeeds)
        verify {
            mockRssRepository.addCustom
        }
    }

    @Test
    fun `get supported source by rss url returns correct item from list`() {
        every { mockRssRepository.supportedSources } returns listOf(primary, secondary)
        initSUT()

        assertEquals(primary.sourceShort, underTest.getSupportedSourceByRssUrl("https://primary.com/rss.xml")!!.sourceShort)
    }

    @Test
    fun `get supported source by rss url returns no items if not found`() {
        every { mockRssRepository.supportedSources } returns listOf(primary, secondary)
        initSUT()

        assertNull(underTest.getSupportedSourceByRssUrl("https://primary.com/rss.xmll"))
    }

    @Test
    fun `enabled reads value from repository`() {
        every { mockRssRepository.enabled } returns true
        initSUT()
        assertTrue(underTest.enabled)
        verify {
            mockRssRepository.enabled
        }
    }

    @ParameterizedTest(name = "get supported source by link with {0} shows {1} item returned")
    @CsvSource(
        "https://www.primary.com,PR",
        "https://primary.com,PR",
        "http://primary.com,PR",
        "http://secondary.com,SE",
        "https://www.secondary.com,SE",
        "https://www.third.com,"
    )
    fun `get supported source by link returns correct item from list`(link: String, expectedSourceShort: String?) {
        every { mockRssRepository.supportedSources } returns listOf(primary, secondary)
        initSUT()

        assertEquals(expectedSourceShort, underTest.getSupportedSourceByLink(link)?.sourceShort)
    }

    @Test
    fun `get supported source by link returns no items if not found`() {
        every { mockRssRepository.supportedSources } returns listOf(primary, secondary)
        initSUT()

        assertNull(underTest.getSupportedSourceByLink("randomlink.com"))
    }

    @ParameterizedTest(name = "stripHost stripping prefixes {0}.stripHTTP() = {1}")
    @CsvSource(
        "https://www.google.com,google.com",
        "http://www.google.com,google.com",
        "https://google.com,google.com",
        "http://google.com,google.com",
        "www.google.com,google.com",
        "ww.google.com,ww.google.com"
    )
    fun `stripHost strips the prefixes off of values`(input: String, expected: String) {
        initSUT()
        assertEquals(expected, underTest.stripHost(input))
    }

    //region Test Data

    private val primary = SupportedSource(
        rssLink = "https://primary.com/rss.xml",
        sourceShort = "PR",
        source = "primary.com",
        colour = "#123123",
        textColour = "#987978",
        title = "Primary",
        contactLink = "https://primary.com/contact"
    )
    private val secondary = SupportedSource(
        rssLink = "https://secondary.com/rss.xml",
        sourceShort = "SE",
        source = "secondary.com",
        colour = "#123123",
        textColour = "#987978",
        title = "Secondary",
        contactLink = "https://secondary.com/contact"
    )

    //endregion

}
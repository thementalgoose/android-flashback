package tmg.flashback.rss.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.rss.testutils.BaseTest

internal class RSSFeedControllerTest: BaseTest() {

    private val mockConfigurationController: ConfigurationController = mockk(relaxed = true)

    private lateinit var sut: RSSFeedController

    private fun initSUT() {
        sut = RSSFeedController(mockConfigurationController)
    }

    @Test
    fun `supported sources maps values from configuration controller`() {

        val expected = SupportedArticleSource("https://www.google.com", "", "", "", "", "", "")

        every { mockConfigurationController.rssSupportedSources } returns listOf(
            SupportedSource("https://www.google.com", "", "", "", "", "", "")
        )
        initSUT()

        assertEquals(listOf(expected), sut.sources)
    }

    @Test
    fun `supported sources returns empty when nothing in configuration manager`() {
        every { mockConfigurationController.rssSupportedSources } returns emptyList()
        initSUT()

        assertEquals(emptyList<SupportedArticleSource>(), sut.sources)
    }

    @Test
    fun `supported sources value doesnt change if configuration changes`() {
        every { mockConfigurationController.rssSupportedSources } returns listOf(
            SupportedSource("https://www.google1.com", "", "", "", "", "", "")
        )
        initSUT()

        assertEquals(1, sut.sources.size)
        every { mockConfigurationController.rssSupportedSources } returns emptyList()
        assertEquals(1, sut.sources.size)
    }

    @Test
    fun `add custom feeds returns custom feed value from configuration manager`() {
        every { mockConfigurationController.rssAddCustom } returns true
        initSUT()

        assertTrue(sut.showAddCustomFeeds)
        verify {
            mockConfigurationController.rssAddCustom
        }
    }

    @Test
    fun `get supported source by rss url returns correct item from list`() {
        every { mockConfigurationController.rssSupportedSources } returns listOf(primary, secondary)
        initSUT()

        assertEquals(primary.sourceShort, sut.getSupportedSourceByRssUrl("https://primary.com/rss.xml")!!.sourceShort)
    }

    @Test
    fun `get supported source by rss url returns no items if not found`() {
        every { mockConfigurationController.rssSupportedSources } returns listOf(primary, secondary)
        initSUT()

        assertNull(sut.getSupportedSourceByRssUrl("https://primary.com/rss.xmll"))
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
        every { mockConfigurationController.rssSupportedSources } returns listOf(primary, secondary)
        initSUT()

        assertEquals(expectedSourceShort, sut.getSupportedSourceByLink(link)?.sourceShort)
    }

    @Test
    fun `get supported source by link returns no items if not found`() {
        every { mockConfigurationController.rssSupportedSources } returns listOf(primary, secondary)
        initSUT()

        assertNull(sut.getSupportedSourceByLink("randomlink.com"))
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
        assertEquals(expected, sut.stripHost(input))
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
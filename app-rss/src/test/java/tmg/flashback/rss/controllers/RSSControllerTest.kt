package tmg.flashback.rss.controllers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.rss.controllers.RSSControllerStub.Companion.primary
import tmg.flashback.rss.testutils.BaseTest

internal class RSSControllerTest: BaseTest() {

    private val stub = RSSControllerStub()

    @ParameterizedTest(name = "RSSController strip host removes turns {0} into {1}")
    @CsvSource(
        "https://www.google.com,google.com",
        "http://www.google.com,google.com",
        "https://google.com,google.com",
        "http://google.com,google.com"
    )
    fun `RSSController strip host removes www and https`(original: String, expected: String) {

        assertEquals(expected, stub.stripHost(original))
    }


    @Test
    fun `RSSController get supported source by rss link`() {

        assertEquals(primary, stub.getSupportedSourceByRssUrl(primary.rssLink))
    }

    @Test
    fun `RSSController get supported source by link`() {

        assertEquals(primary, stub.getSupportedSourceByLink("https://www.rss.com"))

        // Missing protocols, invalid url domains
        assertNull(stub.getSupportedSourceByLink("www.rss.com"))
        assertNull(stub.getSupportedSourceByLink("rss.com"))
        assertNull(stub.getSupportedSourceByLink("http://292ej.gos.xom"))
        assertNull(stub.getSupportedSourceByLink("https://www.rsss.com"))
        assertNull(stub.getSupportedSourceByLink(""))
    }
}
package tmg.flashback.rss.repo.enums

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class SupportedArticleSourceTest {

    @ParameterizedTest(name = "Source {0} is valid == {1}")
    @CsvSource(
            "https://www.autosport.com/test,true",
            "http://www.autosport.com/test,true",
            "https://autosport.com/some/info,true",
            "http://autosport.com/testtting,true",
            "https://www.racefans.net/test,true",
            "https://racefans.net/test,true",
            "http://www.racefans.net/test,true",
            "https://www.autosportt.com/test,false",
            "https://www.autosportt.com/test,false",
            "https://www.randomm.com/test,false",
            "autosport.com/test,false",
    )
    fun `SupportedArticleSource getByLink returning supported link`(link: String, isValid: Boolean) {
        when (isValid) {
            true -> assertNotNull(SupportedArticleSource.getByLink(link))
            false -> assertNull(SupportedArticleSource.getByLink(link))
        }
    }

    @Test
    fun `SupportedArticleSource getByRssFeedURL returns correct supported source with valid RSS feeds`() {

        val supportedItems = 5

        assertEquals(supportedItems, SupportedArticleSource.values().size)

        SupportedArticleSource.values().forEach {
            assertEquals(it, SupportedArticleSource.getByRssFeedURL(it.rssLink))
        }

        assertNull(SupportedArticleSource.getByRssFeedURL("https://www.random.com/feed"))
    }
}
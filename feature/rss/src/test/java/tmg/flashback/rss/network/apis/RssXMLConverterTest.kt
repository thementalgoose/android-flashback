package tmg.flashback.rss.network.apis

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeParseException
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.rss.network.apis.model.RssXMLModel
import tmg.flashback.rss.network.apis.model.RssXMLModelChannel
import tmg.flashback.rss.network.apis.model.RssXMLModelItem
import tmg.flashback.rss.repo.model.Article
import tmg.flashback.rss.repo.model.ArticleSource
import tmg.flashback.rss.testutils.BaseTest

internal class RssXMLConverterTest: BaseTest() {

    private val mockRssFeedController: RSSController = mockk(relaxed = true)
    private val source = "https://www.test.com"

    @Test
    fun `convert returns empty list if channel is null`() {
        val model = RssXMLModel().apply {
            channel = null
        }
        assertEquals(emptyList<Article>(), model.convert(mockRssFeedController, source, false))
    }

    @Test
    fun `convert returns empty list if channel title is null`() {
        val model = RssXMLModel().apply {
            channel = RssXMLModelChannel().apply {
                title = null
            }
        }
        assertEquals(emptyList<Article>(), model.convert(mockRssFeedController, source, false))
    }

    @Test
    fun `convert returns empty list if channel link is null`() {
        val model = RssXMLModel().apply {
            channel = RssXMLModelChannel().apply {
                link = null
                item = emptyList()
            }
        }
        assertEquals(emptyList<Article>(), model.convert(mockRssFeedController, source, false))
    }

    @Test
    fun `convert returns empty list if channel link is not normal URL`() {
        val model = feed(listOf(
            RssXMLModelItem().apply {
                link = "protocol://www.testing.com"
            }
        ))
        assertEquals(emptyList<Article>(), model.convert(mockRssFeedController, source, false))
    }

    @Test
    fun `convert items with no title are not included`() {
        val model = feed(listOf(
            RssXMLModelItem().apply {
                title = null
                description = "hello"
                pubDate = "Sun, 31 Jan 2021 11:13:00 +0000"
                link = "link"
            }
        ))
        assertEquals(emptyList<Article>(), model.convert(mockRssFeedController, source, false))
    }

    @Test
    fun `convert items with no link are not included`() {
        val model = feed(listOf(
            RssXMLModelItem().apply {
                title = "sup"
                description = null
                pubDate = "Sun, 31 Jan 2021 11:13:00 +0000"
                link = null
            }
        ))
        assertEquals(emptyList<Article>(), model.convert(mockRssFeedController, source, false))
    }

    @Test
    fun `convert items with no pubDate are not included`() {
        val model = feed(listOf(
            RssXMLModelItem().apply {
                title = "test"
                description = null
                pubDate = null
                link = "link"
            }
        ))
        assertEquals(emptyList<Article>(), model.convert(mockRssFeedController, source, false))
    }

    @Test
    fun `convert items with invalid pub date throws date time parse exception`() {
        val model = feed(listOf(
            RssXMLModelItem().apply {
                title = "test"
                description = null
                pubDate = "testing - pub - date"
                link = "link"
            }
        ))
        assertThrows(DateTimeParseException::class.java) {
            model.convert(mockRssFeedController, source, false)
        }
    }

    @Test
    fun `convert returns successful conversion model if normal data is returned`() {
        every { mockRssFeedController.getSupportedSourceByLink(any()) } returns null
        val model = feed(listOf(
            RssXMLModelItem().apply {
                title = "sup &#039;"
                description = null
                pubDate = "Sun, 31 Jan 2021 11:13:00 +0000"
                link = "https://www.testing.com/article/1"
            }
        ))

        val expected = Article(
            id = "e42918b365523627bf42aaee5d96fce5",
            title = "sup '",
            description = null,
            link = "https://www.testing.com/article/1",
            date = LocalDateTime.of(2021, 1, 31, 11, 13),
            source = ArticleSource(
                title = "test",
                colour = "#4A34B6",
                textColor = "#FFFFFF",
                rssLink = source,
                source = source,
                shortSource = null,
                contactLink = null
            )
        )

        assertEquals(listOf(expected), model.convert(mockRssFeedController, source, false))
    }

    @Test
    fun `convert returns successful conversion model if normal data is returned with description being null if show is false`() {
        every { mockRssFeedController.getSupportedSourceByLink(any()) } returns null
        val model = feed(listOf(
            RssXMLModelItem().apply {
                title = "sup"
                description = "test description"
                pubDate = "Sun, 31 Jan 2021 11:13:00 +0000"
                link = "https://www.testing.com/article/1"
            }
        ))

        val expected = Article(
            id = "e42918b365523627bf42aaee5d96fce5",
            title = "sup",
            description = null,
            link = "https://www.testing.com/article/1",
            date = LocalDateTime.of(2021, 1, 31, 11, 13),
            source = ArticleSource(
                title = "test",
                colour = "#4A34B6",
                textColor = "#FFFFFF",
                rssLink = source,
                source = source,
                shortSource = null,
                contactLink = null
            )
        )

        assertEquals(listOf(expected), model.convert(mockRssFeedController, source, false))
    }

    //region Test Data

    private fun feed(list: List<RssXMLModelItem>): RssXMLModel {
        return RssXMLModel().apply {
            channel = RssXMLModelChannel().apply {
                link = source
                title = "test"
                item = list
            }
        }
    }

    //endregion
}
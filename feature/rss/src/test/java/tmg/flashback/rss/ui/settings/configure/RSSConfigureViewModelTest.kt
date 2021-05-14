package tmg.flashback.rss.ui.settings.configure

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.rss.R
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.rss.prefs.RSSRepositoryI
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.rss.testutils.BaseTest
import tmg.flashback.rss.testutils.assertDataEventValue
import tmg.flashback.rss.testutils.assertListDoesNotMatchItem
import tmg.flashback.rss.testutils.test

class RSSConfigureViewModelTest: BaseTest() {

    lateinit var sut: RSSConfigureViewModel

    private val mock: RSSRepositoryI = mockk(relaxed = true)
    private val mockRssFeedController: RSSController = mockk(relaxed = true)

    private val mockSupportedArticle = SupportedArticleSource("https://www.test.com/rss", "", "https://www.test.com", "", "", "", "https://www.test.com/contact")
    private val mockListOfSupportedArticles: List<SupportedArticleSource> = listOf(mockSupportedArticle)

    @BeforeEach
    fun setUp() {
        every { mockRssFeedController.showAddCustomFeeds } returns true
        every { mock.rssUrls } returns emptySet()
        every { mockRssFeedController.sources } returns mockListOfSupportedArticles
    }

    private fun initSUT() {
        sut = RSSConfigureViewModel(mock, mockRssFeedController)
    }

    @Test
    fun `list is initialised with no items shown by default`() {

        initSUT()

        val expected = buildList(
            added = emptyList(),
            quick = mockListOfSupportedArticles
        )

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `add quick item will update the prefs DB`() {

        every { mock.rssUrls } returns emptySet()

        initSUT()

        sut.inputs.addQuickItem(mockListOfSupportedArticles.first())

        verify { mock.rssUrls = setOf(mockSupportedArticle.rssLink) }
    }

    @Test
    fun `removing item will update the prefs DB`() {

        val link = mockSupportedArticle.rssLink
        every { mock.rssUrls } returns setOf(link)

        initSUT()

        sut.inputs.removeItem(link)

        verify { mock.rssUrls = emptySet() }
    }

    @Test
    fun `visit website fires open website event`() {

        initSUT()
        sut.inputs.visitWebsite(mockSupportedArticle)

        sut.outputs.openWebsite.test {
            assertDataEventValue(mockSupportedArticle)
        }
    }

    @Test
    fun `disabling add custom list toggle means section is not shown `() {

        every { mockRssFeedController.showAddCustomFeeds } returns false
        every { mockRssFeedController.getSupportedSourceByRssUrl(any()) } returns null
        initSUT()

        sut.outputs.list.test {
            assertListDoesNotMatchItem { it is RSSConfigureItem.Add }
            assertListDoesNotMatchItem { it is RSSConfigureItem.Header && it.text == R.string.rss_configure_header_add }
        }
    }

    @Test
    fun `adding custom item updates list`() {

        val item = "https://www.google.com/testlink"
        val expected = buildList(
                added = listOf(item),
                quick = mockListOfSupportedArticles
        )
        every { mockRssFeedController.getSupportedSourceByRssUrl(any()) } returns null
        initSUT()

        // Assume preferences updated
        every { mock.rssUrls } returns setOf(item)
        sut.inputs.addCustomItem(item)

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `removing custom item updates list`() {

        val item = "https://www.google.com/testlink"
        val expected = buildList(
            added = emptyList()
        )
        every { mock.rssUrls } returns setOf(item)

        initSUT()
        // Assume preferences updated
        every { mock.rssUrls } returns emptySet()
        sut.inputs.removeItem(item)

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `adding custom item updates value in preferences`() {

        val item = "https://www.google.com/testlink"
        val expected = setOf(item)
        every { mock.rssUrls } returns emptySet()

        initSUT()
        sut.inputs.addCustomItem(item)

        verify { mock.rssUrls = expected }
    }

    @Test
    fun `removing custom item updates value in preferences`() {

        val item = "https://www.google.com/testlink"
        val expected = emptySet<String>()
        every { mock.rssUrls } returns setOf(item)

        initSUT()
        sut.inputs.removeItem(item)

        verify { mock.rssUrls = expected }
    }


    private fun buildList(
        added: List<String>,
        quick: List<SupportedArticleSource> = mockListOfSupportedArticles
    ): List<RSSConfigureItem> {
        val list = mutableListOf<RSSConfigureItem>()
        list.add(RSSConfigureItem.Header(R.string.rss_configure_header_items, R.string.rss_configure_header_items_subtitle))
        if (added.isEmpty()) {
            list.add(RSSConfigureItem.NoItems)
        }
        else {
            list.addAll(added
                .sortedBy { it
                    .replace("https://www.", "")
                    .replace("http://www.", "")
                    .replace("https://", "")
                    .replace("http://", "")
                }
                .map {
                    RSSConfigureItem.Item(it, null)
                }
            )
        }
        list.add(RSSConfigureItem.Header(R.string.rss_configure_header_add, R.string.rss_configure_header_add_subtitle))
        list.add(RSSConfigureItem.Add)
        list.add(RSSConfigureItem.Header(R.string.rss_configure_header_quick_add, R.string.rss_configure_header_quick_add_subtitle))
        list.addAll(quick
            .sortedBy { it.rssLink
                .replace("https://www.", "")
                .replace("http://www.", "")
                .replace("https://", "")
                .replace("http://", "")
            }
            .map {
                RSSConfigureItem.QuickAdd(it)
            }
        )
        return list
    }
}

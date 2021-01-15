package tmg.flashback.rss.ui.configure

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import tmg.flashback.rss.R
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.rss.prefs.RSSPrefsRepository
import tmg.flashback.rss.repo.enums.SupportedArticleSource
import tmg.flashback.rss.testutils.BaseTest
import tmg.flashback.rss.testutils.assertDataEventValue
import tmg.flashback.rss.testutils.test
import java.util.stream.Stream

class RSSConfigureViewModelTest: BaseTest() {

    lateinit var sut: RSSConfigureViewModel

    private val mockPrefs: RSSPrefsRepository = mockk(relaxed = true)
    private val mockRssController: RSSController = mockk(relaxed = true)

    private val mockSupportedArticle = SupportedArticleSource("https://www.test.com/rss", "", "https://www.test.com", "", "", "", "https://www.test.com/contact")
    private val mockListOfSupportedArticles: List<SupportedArticleSource> = listOf(mockSupportedArticle)

    @BeforeEach
    fun setUp() {
        every { mockPrefs.rssUrls } returns emptySet()
        every { mockRssController.sources } returns mockListOfSupportedArticles
    }

    private fun initSUT() {
        sut = RSSConfigureViewModel(mockPrefs, mockRssController)
    }

    @Test
    fun `RSSConfigureViewModel list is initialised with no items shown by default`() {

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
    fun `RSSConfigureViewModel add quick item will update the prefs DB`() {

        every { mockPrefs.rssUrls } returns emptySet()

        initSUT()

        sut.inputs.addQuickItem(mockListOfSupportedArticles.first())

        verify { mockPrefs.rssUrls = setOf(mockSupportedArticle.rssLink) }
    }

    @Test
    fun `RSSConfigureViewModel removing item will update the prefs DB`() {

        val link = mockSupportedArticle.rssLink
        every { mockPrefs.rssUrls } returns setOf(link)

        initSUT()

        sut.inputs.removeItem(link)

        verify { mockPrefs.rssUrls = emptySet() }
    }

    @Test
    fun `RSSConfigureViewModel visit website fires open website event`() {

        initSUT()
        sut.inputs.visitWebsite(mockSupportedArticle)

        sut.outputs.openWebsite.test {
            assertDataEventValue(mockSupportedArticle)
        }
    }

    @Test
    fun `RSSConfigureViewModel adding custom item updates list`() {

        val item = "https://www.google.com/testlink"
        val expected = buildList(
            added = listOf(item),
            quick = mockListOfSupportedArticles
        )
        every { mockRssController.getSupportedSourceByRssUrl(any()) } returns null
        initSUT()

        // Assume preferences updated
        every { mockPrefs.rssUrls } returns setOf(item)
        sut.inputs.addCustomItem(item)

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `RSSConfigureViewModel removing custom item updates list`() {

        val item = "https://www.google.com/testlink"
        val expected = buildList(
            added = emptyList()
        )
        every { mockPrefs.rssUrls } returns setOf(item)

        initSUT()
        // Assume preferences updated
        every { mockPrefs.rssUrls } returns emptySet()
        sut.inputs.removeItem(item)

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `RSSConfigureViewModel adding custom item updates value in preferences`() {

        val item = "https://www.google.com/testlink"
        val expected = setOf(item)
        every { mockPrefs.rssUrls } returns emptySet()

        initSUT()
        sut.inputs.addCustomItem(item)

        verify { mockPrefs.rssUrls = expected }
    }

    @Test
    fun `RSSConfigureViewModel removing custom item updates value in preferences`() {

        val item = "https://www.google.com/testlink"
        val expected = emptySet<String>()
        every { mockPrefs.rssUrls } returns setOf(item)

        initSUT()
        sut.inputs.removeItem(item)

        verify { mockPrefs.rssUrls = expected }
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

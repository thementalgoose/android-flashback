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
import tmg.flashback.rss.prefs.RSSPrefsRepository
import tmg.flashback.rss.repo.enums.SupportedArticleSource
import tmg.flashback.rss.testutils.BaseTest
import tmg.flashback.rss.testutils.test
import java.util.stream.Stream

class RSSConfigureViewModelTest: BaseTest() {

    lateinit var sut: RSSConfigureViewModel

    private val mockPrefs: RSSPrefsRepository = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        every { mockPrefs.rssUrls } returns emptySet()
    }

    private fun initSUT() {
        sut = RSSConfigureViewModel(mockPrefs)
    }

    @Test
    fun `RSSConfigureViewModel list is initialised with no items shown by default`() {

        initSUT()

        val expected = buildList(
            added = emptyList(),
            quick = SupportedArticleSource.values().toList()
        )

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `RSSConfigureViewModel add quick item will update the prefs DB`() {

        val link = SupportedArticleSource.AUTOSPORT
        every { mockPrefs.rssUrls } returns emptySet()

        initSUT()

        sut.inputs.addQuickItem(link)

        verify { mockPrefs.rssUrls = setOf(link.rssLink) }
    }

    @ParameterizedTest
    @MethodSource("allSupportedArticles")
    fun `RSSConfigureViewModel simulate adding single item to update the list properly`(source: SupportedArticleSource) {

        val expected = buildList(
            added = listOf(source.rssLink),
            quick = SupportedArticleSource.values()
                .filter { it != source }
        )
        every { mockPrefs.rssUrls } returns emptySet()

        initSUT()

        // Assume PrefsDB is saving item properly (tested previously)
        every { mockPrefs.rssUrls } returns setOf(source.rssLink)
        sut.inputs.removeItem(source.rssLink)

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `RSSConfigureViewModel removing item will update the prefs DB`() {

        val link = SupportedArticleSource.AUTOSPORT.rssLink
        every { mockPrefs.rssUrls } returns setOf(link)

        initSUT()

        sut.inputs.removeItem(link)

        verify { mockPrefs.rssUrls = emptySet() }
    }

    @ParameterizedTest
    @MethodSource("allSupportedArticles")
    fun `RSSConfigureViewModel simulate removing single item to update the list properly`(source: SupportedArticleSource) {

        val expected = buildList(
            added = SupportedArticleSource.values()
                .filter { it != source }
                .map { it.rssLink },
            quick = listOf(source)
        )
        every { mockPrefs.rssUrls } returns SupportedArticleSource.values()
            .map { it.rssLink }
            .toSet()

        initSUT()

        // Assume PrefsDB is saving item properly (tested previously)
        every { mockPrefs.rssUrls } returns SupportedArticleSource.values()
            .filter { it != source }
            .map { it.rssLink }
            .toSet()

        sut.inputs.removeItem(source.rssLink)

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `RSSConfigureViewModel adding custom item updates list`() {

        val item = "https://www.google.com/testlink"
        val expected = buildList(
            added = listOf(item)
        )
        every { mockPrefs.rssUrls } returns emptySet()

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

    companion object {
        @JvmStatic
        fun allSupportedArticles(): Stream<Arguments> = Stream.of(
            *SupportedArticleSource.values()
                .map {
                    Arguments.of(it)
                }
                .toTypedArray()
        )
    }


    private fun buildList(
        added: List<String>,
        quick: List<SupportedArticleSource> = SupportedArticleSource.values().toList()
    ): List<RSSConfigureItem> {
        val list = mutableListOf<RSSConfigureItem>()
        list.add(RSSConfigureItem.Header(R.string.rss_configure_header_items, R.string.rss_configure_header_items_subtitle))
        if (added.isEmpty()) {
            list.add(RSSConfigureItem.NoItems)
        }
        else {
            list.addAll(added
                .sortedBy { it }
                .map {
                    RSSConfigureItem.Item(it)
                }
            )
        }
        list.add(RSSConfigureItem.Header(R.string.rss_configure_header_add, R.string.rss_configure_header_add_subtitle))
        list.add(RSSConfigureItem.Add)
        list.add(RSSConfigureItem.Header(R.string.rss_configure_header_quick_add, R.string.rss_configure_header_quick_add_subtitle))
        list.addAll(quick
            .sortedBy { it.source }
            .map {
                RSSConfigureItem.QuickAdd(it)
            }
        )
        return list
    }
}

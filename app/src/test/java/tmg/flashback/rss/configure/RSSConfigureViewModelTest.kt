package tmg.flashback.rss.configure

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import tmg.flashback.R
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.enums.SupportedArticleSource
import tmg.flashback.rss.settings.RSSSettingsViewModel
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertValue
import java.util.stream.Stream

class RSSConfigureViewModelTest: BaseTest() {

    lateinit var sut: RSSConfigureViewModel

    private val mockPrefs: PrefsDB = mock()

    @BeforeEach
    fun setUp() {
        whenever(mockPrefs.rssUrls).thenReturn(emptySet())
    }

    private fun initSUT() {
        sut = RSSConfigureViewModel(mockPrefs, testScopeProvider)
    }

    @Test
    fun `RSSConfigureViewModel list is initialised with no items shown by default`() {

        initSUT()

        val expected = buildList(
            added = emptyList(),
            quick = SupportedArticleSource.values().toList()
        )

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `RSSConfigureViewModel add quick item will update the prefs DB`() {

        val link = SupportedArticleSource.AUTOSPORT
        whenever(mockPrefs.rssUrls).thenReturn(emptySet())

        initSUT()

        sut.inputs.addQuickItem(link)

        verify(mockPrefs).rssUrls = setOf(link.rssLink)
    }

    @ParameterizedTest
    @MethodSource("allSupportedArticles")
    fun `RSSConfigureViewModel simulate adding single item to update the list properly`(source: SupportedArticleSource) {

        val expected = buildList(
            added = listOf(source.rssLink),
            quick = SupportedArticleSource.values()
                .filter { it != source }
        )
        whenever(mockPrefs.rssUrls).thenReturn(emptySet())

        initSUT()

        // Assume PrefsDB is saving item properly (tested previously)
        whenever(mockPrefs.rssUrls).thenReturn(setOf(source.rssLink))
        sut.inputs.removeItem(source.rssLink)

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `RSSConfigureViewModel removing item will update the prefs DB`() {

        val link = SupportedArticleSource.AUTOSPORT.rssLink
        whenever(mockPrefs.rssUrls).thenReturn(setOf(link))

        initSUT()

        sut.inputs.removeItem(link)

        verify(mockPrefs).rssUrls = emptySet()
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
        whenever(mockPrefs.rssUrls).thenReturn(SupportedArticleSource.values().map { it.rssLink }.toSet())

        initSUT()

        // Assume PrefsDB is saving item properly (tested previously)
        whenever(mockPrefs.rssUrls).thenReturn(SupportedArticleSource.values()
            .filter { it != source }
            .map { it.rssLink }
            .toSet()
        )
        sut.inputs.removeItem(source.rssLink)

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `RSSConfigureViewModel adding custom item updates list`() {

        val item = "https://www.google.com/testlink"
        val expected = buildList(
            added = listOf(item)
        )
        whenever(mockPrefs.rssUrls).thenReturn(emptySet())

        initSUT()

        // Assume preferences updated
        whenever(mockPrefs.rssUrls).thenReturn(setOf(item))
        sut.inputs.addCustomItem(item)

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `RSSConfigureViewModel removing custom item updates list`() {

        val item = "https://www.google.com/testlink"
        val expected = buildList(
            added = emptyList()
        )
        whenever(mockPrefs.rssUrls).thenReturn(setOf(item))

        initSUT()
        // Assume preferences updated
        whenever(mockPrefs.rssUrls).thenReturn(emptySet())
        sut.inputs.removeItem(item)

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `RSSConfigureViewModel adding custom item updates value in preferences`() {

        val item = "https://www.google.com/testlink"
        val expected = setOf(item)
        whenever(mockPrefs.rssUrls).thenReturn(emptySet())

        initSUT()
        sut.inputs.addCustomItem(item)

        verify(mockPrefs).rssUrls = expected
    }

    @Test
    fun `RSSConfigureViewModel removing custom item updates value in preferences`() {

        val item = "https://www.google.com/testlink"
        val expected = emptySet<String>()
        whenever(mockPrefs.rssUrls).thenReturn(setOf(item))

        initSUT()
        sut.inputs.removeItem(item)

        verify(mockPrefs).rssUrls = expected
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

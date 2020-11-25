package tmg.flashback.rss.ui

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.repo.NetworkConnectivityManager
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.db.news.RSSDB
import tmg.flashback.repo.enums.SupportedArticleSource
import tmg.flashback.repo.models.Response
import tmg.flashback.repo.models.rss.Article
import tmg.flashback.repo.models.rss.ArticleSource
import tmg.flashback.rss.prefs.RSSPrefsDB
import tmg.flashback.rss.ui.RSSItem
import tmg.flashback.rss.ui.RSSViewModel
import tmg.flashback.testutils.*

@FlowPreview
@ExperimentalCoroutinesApi
class RSSViewModelTest: BaseTest() {

    private lateinit var sut: RSSViewModel

    private val mockRSSDB: RSSDB = mock()
    private val mockPrefsDB: RSSPrefsDB = mock()
    private val mockConnectivityManager: NetworkConnectivityManager = mock()

    private val mockLocalDate: LocalDateTime = LocalDateTime.of(2020, 1, 1, 1, 2, 3, 0)
    private val mockArticleSource = ArticleSource(
        title = "Test source",
        colour = "#123456",
        shortSource = "short something here",
        rssLink = "https://www.google.com/rss",
        source = "https://www.google.com/source"
    )
    private val mockArticle = Article(
        id = "test",
        title = "test title",
        description = "test description",
        link = "http://www.google.com",
        date = mockLocalDate,
        source = mockArticleSource
    )

    private val mockResponse200 = flow { emit(Response(listOf(mockArticle))) }
    private val mockResponse500 = flow { emit(Response<List<Article>>(null)) }
    private val mockResponseNoNetwork = flow { emit(Response<List<Article>>(null, -1)) }

    @BeforeEach
    internal fun setUp() {

        whenever(mockConnectivityManager.isConnected).thenReturn(true)
        whenever(mockPrefsDB.rssUrls).thenReturn(SupportedArticleSource.values().map { it.rssLink }.toSet())
        whenever(mockPrefsDB.rssShowDescription).thenReturn(true)
        whenever(mockRSSDB.getNews()).thenReturn(mockResponse200)
    }

    private fun initSUT() {

        sut = RSSViewModel(
            mockRSSDB,
            mockPrefsDB,
            mockConnectivityManager,
            testScopeProvider
        )
    }

    @Test
    fun `RSSViewModel is refreshing is initialised is reset after refresh flow`() = coroutineTest {

        initSUT()

        val observer = sut.outputs.isRefreshing.test()
        advanceUntilIdle()

        assertListContains(sut.outputs.list) {
            it == RSSItem.RSS(mockArticle) || it is RSSItem.Message
        }
        assertEquals(listOf(true, false), observer.listOfValues.toList())

        sut.inputs.refresh()

        advanceUntilIdle()

        assertEquals(listOf(true, false, true, false), observer.listOfValues.toList())
    }

    @Test
    fun `RSSViewModel init loads all news sources`() = coroutineTest {

        val expected = listOf(
            RSSItem.RSS(mockArticle)
        )

        initSUT()
        advanceUntilIdle()

        assertListContains(sut.outputs.list) {
            it == RSSItem.RSS(mockArticle) ||
                    (it is RSSItem.Message && it.msg.split(":").size == 3)
        }
    }

    @Test
    fun `RSSViewModel init all sources disabled if excludes list contains all news sources`() = coroutineTest {

        whenever(mockRSSDB.getNews()).thenReturn(mockResponse500)
        whenever(mockPrefsDB.rssUrls).thenReturn(emptySet())

        val expected = listOf<RSSItem>(
            RSSItem.SourcesDisabled
        )

        initSUT()
        advanceUntilIdle()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `RSSViewModel init internal error is thrown if results are empty`() = coroutineTest {

        whenever(mockRSSDB.getNews()).thenReturn(mockResponse500)

        val expected = listOf<RSSItem>(
            RSSItem.InternalError
        )

        initSUT()
        advanceUntilIdle()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `RSSViewModel no network error shown when network response code is no network`() = coroutineTest {

        whenever(mockRSSDB.getNews()).thenReturn(mockResponseNoNetwork)

        val expected = listOf<RSSItem>(
            RSSItem.NoNetwork
        )

        initSUT()
        advanceUntilIdle()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `RSSViewModel no network error shown when network connectivity check returns false`() = coroutineTest {

        whenever(mockConnectivityManager.isConnected).thenReturn(false)

        val expected = listOf<RSSItem>(
            RSSItem.NoNetwork
        )

        initSUT()
        advanceUntilIdle()

        assertValue(expected, sut.outputs.list)
    }


    @AfterEach
    internal fun tearDown() {

        reset(mockRSSDB, mockPrefsDB, mockConnectivityManager)
    }
}
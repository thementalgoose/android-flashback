package tmg.flashback.rss.ui

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDateTime
import tmg.flashback.repo.models.Response
import tmg.flashback.rss.managers.RSSNetworkConnectivityManager
import tmg.flashback.rss.prefs.RSSPrefsRepository
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.repo.enums.SupportedArticleSource
import tmg.flashback.rss.repo.model.Article
import tmg.flashback.rss.repo.model.ArticleSource
import tmg.flashback.rss.testutils.BaseTest
import tmg.flashback.rss.testutils.*

class RSSViewModelTest: BaseTest() {

    private lateinit var sut: RSSViewModel

    private val mockRSSDB: RSSRepository = mockk()
    private val mockPrefsRepository: RSSPrefsRepository = mockk()
    private val mockConnectivityManager: RSSNetworkConnectivityManager = mockk()

    private val mockLocalDate: LocalDateTime = LocalDateTime.of(2020, 1, 1, 1, 2, 3, 0)
    private val mockArticleSource = ArticleSource(
        title = "Test source",
        colour = "#123456",
        shortSource = "short something here",
        rssLink = "https://www.google.com/rss",
        source = "https://www.google.com/source",
        contactLink = null
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

        every { mockConnectivityManager.isConnected } returns true
        every { mockPrefsRepository.rssUrls } returns setOf("https://www.mock.rss.url.com")
        every { mockPrefsRepository.rssShowDescription } returns true
        every { mockRSSDB.getNews() } returns mockResponse200
    }

    private fun initSUT() {

        sut = RSSViewModel(
            mockRSSDB,
            mockPrefsRepository,
            mockConnectivityManager
        )
    }

    @Test
    fun `RSSViewModel is refreshing is initialised is reset after refresh flow`() = coroutineTest {

        initSUT()

        val observer = sut.outputs.isRefreshing.testObserve()
        advanceUntilIdle()

        sut.outputs.list.test {
            assertListContainsItem(RSSItem.RSS(mockArticle))
            assertListHasItem { it is RSSItem.Message }
        }

        observer.assertEmittedItems(true, false)

        sut.inputs.refresh()

        advanceUntilIdle()

        observer.assertEmittedItems(true, false, true, false)
    }

    @Test
    fun `RSSViewModel init loads all news sources`() = coroutineTest {

        initSUT()
        advanceUntilIdle()

        sut.outputs.list.test {
            assertListContainsItem(RSSItem.RSS(mockArticle))
            assertListHasItem { it is RSSItem.Message && it.msg.split(":").size == 3 }
        }
    }

    @Test
    fun `RSSViewModel init all sources disabled if excludes list contains all news sources`() = coroutineTest {

        every { mockRSSDB.getNews() } returns mockResponse500
        every { mockPrefsRepository.rssUrls } returns emptySet()

        val expected = listOf<RSSItem>(
            RSSItem.SourcesDisabled
        )

        initSUT()
        advanceUntilIdle()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `RSSViewModel init internal error is thrown if results are empty`() = coroutineTest {

        every { mockRSSDB.getNews() } returns mockResponse500

        val expected = listOf<RSSItem>(
            RSSItem.InternalError
        )

        initSUT()
        advanceUntilIdle()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `RSSViewModel no network error shown when network response code is no network`() = coroutineTest {

        every { mockRSSDB.getNews() } returns mockResponseNoNetwork

        val expected = listOf<RSSItem>(
            RSSItem.NoNetwork
        )

        initSUT()
        advanceUntilIdle()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `RSSViewModel no network error shown when network connectivity check returns false`() = coroutineTest {

        every { mockConnectivityManager.isConnected } returns false

        val expected = listOf<RSSItem>(
            RSSItem.NoNetwork
        )

        initSUT()
        advanceUntilIdle()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }
}
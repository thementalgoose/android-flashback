package tmg.flashback.rss.ui

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDateTime
import tmg.flashback.ads.repository.AdsRepository
import tmg.flashback.ads.repository.model.AdvertConfig
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.repo.RssAPI
import tmg.flashback.rss.repo.model.Article
import tmg.flashback.rss.repo.model.ArticleSource
import tmg.flashback.rss.repo.model.Response
import tmg.testutils.BaseTest
import tmg.testutils.livedata.*

class RSSViewModelTest: BaseTest() {

    private lateinit var sut: RSSViewModel

    private val mockRSSDB: RssAPI = mockk(relaxed = true)
    private val mockRssRepository: RSSRepository = mockk(relaxed = true)
    private val mockAdsRepository: AdsRepository = mockk(relaxed = true)
    private val mockConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)

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

    private val mockResponse200 = flow { emit(Response<List<Article>>(listOf(mockArticle))) }
    private val mockResponse500 = flow { emit(Response<List<Article>>(null)) }
    private val mockResponseNoNetwork = flow { emit(Response<List<Article>>(null, -1)) }

    @BeforeEach
    internal fun setUp() {

        every { mockConnectivityManager.isConnected } returns true
        every { mockRssRepository.rssUrls } returns setOf("https://www.mock.rss.url.com")
        every { mockRssRepository.rssShowDescription } returns true
        every { mockRSSDB.getNews() } returns mockResponse200
        every { mockAdsRepository.advertConfig } returns AdvertConfig(
            onRss = true
        )
    }

    private fun initSUT() {

        sut = RSSViewModel(
            mockRSSDB,
            mockRssRepository,
            mockAdsRepository,
            mockConnectivityManager
        )
        sut.refresh()
    }

    @Test
    fun `is refreshing is initialised is reset after refresh flow`() = coroutineTest {

        initSUT()

        val observer = sut.outputs.isRefreshing.testObserve()
        advanceUntilIdle()

        sut.outputs.list.test {
            assertListContainsItem(RSSItem.RSS(mockArticle))
            assertListContainsItem(RSSItem.Advert)
            assertListMatchesItem { it is RSSItem.Message }
        }

        observer.assertEmittedItems(true, false)

        sut.inputs.refresh()

        advanceUntilIdle()

        observer.assertEmittedItems(true, false, true, false)
    }

    @Test
    fun `init with ads disabled doesnt show advert item`() = coroutineTest {
        every { mockAdsRepository.advertConfig } returns AdvertConfig(onRss = false)

        initSUT()

        advanceUntilIdle()

        sut.outputs.list.test {
            assertListContainsItem(RSSItem.RSS(mockArticle))
            assertListDoesNotMatchItem { it is RSSItem.Advert }
        }
    }

    @Test
    fun `init loads all news sources`() = coroutineTest {

        initSUT()
        advanceUntilIdle()

        sut.outputs.list.test {
            assertListContainsItem(RSSItem.RSS(mockArticle))
            assertListContainsItem(RSSItem.Advert)
            assertListMatchesItem { it is RSSItem.Message && it.msg.split(":").size == 3 }
        }
    }

    @Test
    fun `init all sources disabled if excludes list contains all news sources`() = coroutineTest {

        every { mockRSSDB.getNews() } returns mockResponse500
        every { mockRssRepository.rssUrls } returns emptySet()

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
    fun `init internal error is thrown if results are empty`() = coroutineTest {

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
    fun `no network error shown when network response code is no network`() = coroutineTest {

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
    fun `no network error shown when network connectivity check returns false`() = coroutineTest {

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
package tmg.flashback.rss.ui.feed

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDateTime
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.ads.ads.repository.model.AdvertConfig
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.rss.RSSConfigure
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.repo.RssAPI
import tmg.flashback.rss.repo.model.Article
import tmg.flashback.rss.repo.model.ArticleSource
import tmg.flashback.rss.repo.model.Response
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertListContainsItem
import tmg.testutils.livedata.assertListDoesNotMatchItem
import tmg.testutils.livedata.assertListMatchesItem
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve

internal class RSSViewModelTest: BaseTest() {

    private lateinit var underTest: RSSViewModel

    private val mockRSSDB: RssAPI = mockk(relaxed = true)
    private val mockRssRepository: RSSRepository = mockk(relaxed = true)
    private val mockAdsRepository: AdsRepository = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)
    private val mockOpenWebpageUseCase: OpenWebpageUseCase = mockk(relaxed = true)
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

    private fun initUnderTest() {

        underTest = RSSViewModel(
            RSSDB = mockRSSDB,
            rssRepository = mockRssRepository,
            adsRepository = mockAdsRepository,
            navigator = mockNavigator,
            openWebpageUseCase = mockOpenWebpageUseCase,
            connectivityManager = mockConnectivityManager
        )
        underTest.refresh()
    }

    @Test
    fun `is refreshing is initialised is reset after refresh flow`() = coroutineTest {

        initUnderTest()

        val observer = underTest.outputs.isRefreshing.testObserve()
        advanceUntilIdle()

        underTest.outputs.list.test {
            assertListContainsItem(RSSModel.RSS(mockArticle))
            assertListContainsItem(RSSModel.Advert)
            assertListMatchesItem { it is RSSModel.Message }
        }

        observer.assertEmittedItems(true, false)

        underTest.inputs.refresh()

        advanceUntilIdle()

        observer.assertEmittedItems(true, false, true, false)
    }

    @Test
    fun `init with ads disabled doesnt show advert item`() = coroutineTest {
        every { mockAdsRepository.advertConfig } returns AdvertConfig(
            onRss = false
        )

        initUnderTest()

        advanceUntilIdle()

        underTest.outputs.list.test {
            assertListContainsItem(RSSModel.RSS(mockArticle))
            assertListDoesNotMatchItem { it is RSSModel.Advert }
        }
    }

    @Test
    fun `init loads all news sources`() = coroutineTest {

        initUnderTest()
        advanceUntilIdle()

        underTest.outputs.list.test {
            assertListContainsItem(RSSModel.RSS(mockArticle))
            assertListContainsItem(RSSModel.Advert)
            assertListMatchesItem { it is RSSModel.Message && it.msg.split(":").size == 3 }
        }
    }

    @Test
    fun `init all sources disabled if excludes list contains all news sources`() = coroutineTest {

        every { mockRSSDB.getNews() } returns mockResponse500
        every { mockRssRepository.rssUrls } returns emptySet()

        val expected = listOf<RSSModel>(
            RSSModel.SourcesDisabled
        )

        initUnderTest()
        advanceUntilIdle()

        underTest.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `init internal error is thrown if results are empty`() = coroutineTest {

        every { mockRSSDB.getNews() } returns mockResponse500

        val expected = listOf<RSSModel>(
            RSSModel.InternalError
        )

        initUnderTest()
        advanceUntilIdle()

        underTest.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `no network error shown when network response code is no network`() = coroutineTest {

        every { mockRSSDB.getNews() } returns mockResponseNoNetwork

        val expected = listOf<RSSModel>(
            RSSModel.NoNetwork
        )

        initUnderTest()
        advanceUntilIdle()

        underTest.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `no network error shown when network connectivity check returns false`() = coroutineTest {

        every { mockConnectivityManager.isConnected } returns false

        val expected = listOf<RSSModel>(
            RSSModel.NoNetwork
        )

        initUnderTest()
        advanceUntilIdle()

        underTest.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `click configure opens rss settings`() = coroutineTest {

        initUnderTest()
        advanceUntilIdle()

        underTest.inputs.configure()

        verify {
            mockNavigator.navigate(Screen.Settings.RSSConfigure)
        }
    }

    @Test
    fun `click model with open in external browser disabled opens in in-app browser`() {
        val model: RSSModel.RSS = mockk {
            every { item } returns mockk(relaxed = true)
        }
        initUnderTest()

        underTest.inputs.clickModel(model)

        verify {
            mockOpenWebpageUseCase.open(any(), any())
        }
    }
}
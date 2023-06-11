package tmg.flashback.rss.ui.feed

import app.cash.turbine.Event
import app.cash.turbine.test
import app.cash.turbine.testIn
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDateTime
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.ads.ads.repository.model.AdvertConfig
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.rss.contract.RSSConfigure
import tmg.flashback.rss.network.RssService
import tmg.flashback.rss.repo.RssRepository
import tmg.flashback.rss.repo.model.Article
import tmg.flashback.rss.repo.model.ArticleSource
import tmg.flashback.rss.repo.model.Response
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.testutils.BaseTest

internal class RSSViewModelTest: BaseTest() {

    private lateinit var underTest: RSSViewModel

    private val mockRssService: RssService = mockk(relaxed = true)
    private val mockRssRepository: RssRepository = mockk(relaxed = true)
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
        every { mockRssService.getNews() } returns mockResponse200
        every { mockAdsRepository.advertConfig } returns AdvertConfig(
            onRss = true
        )
    }

    private fun initUnderTest() {

        underTest = RSSViewModel(
            rssService = mockRssService,
            rssRepository = mockRssRepository,
            adsRepository = mockAdsRepository,
            navigator = mockNavigator,
            openWebpageUseCase = mockOpenWebpageUseCase,
            connectivityManager = mockConnectivityManager
        )
        underTest.refresh()
    }

    @Test
    fun `is refreshing is initialised is reset after refresh flow`() = runTest {

        initUnderTest()

        val observer = underTest.outputs.isRefreshing.testIn(this)
        advanceUntilIdle()

        underTest.outputs.list.test {
            val item = awaitItem()
            assertTrue(item.any { it == RSSModel.RSS(mockArticle) })
            assertTrue(item.any { it == RSSModel.Advert })
            assertTrue(item.any { it is RSSModel.Message })
        }

        underTest.inputs.refresh()
        advanceUntilIdle()

        val events = observer.cancelAndConsumeRemainingEvents()
        assertEquals(false, (events[0] as Event.Item<Boolean>).value) // Initial
        assertEquals(true, (events[1] as Event.Item<Boolean>).value)
        assertEquals(false, (events[2] as Event.Item<Boolean>).value) // After init refresh
        assertEquals(true, (events[3] as Event.Item<Boolean>).value)
        assertEquals(false, (events[4] as Event.Item<Boolean>).value) // After second refresh
    }

    @Test
    fun `init with ads disabled doesnt show advert item`() = runTest {
        every { mockAdsRepository.advertConfig } returns AdvertConfig(
            onRss = false
        )

        initUnderTest()

        advanceUntilIdle()

        underTest.outputs.list.test {
            val item = awaitItem()
            assertTrue(item.any { it == RSSModel.RSS(mockArticle) })
            assertTrue(item.none { it == RSSModel.Advert })
        }
    }

    @Test
    fun `init loads all news sources`() = runTest {

        initUnderTest()
        advanceUntilIdle()

        underTest.outputs.list.test {
            val item = awaitItem()
            assertTrue(item.any { it == RSSModel.RSS(mockArticle) })
            assertTrue(item.any { it == RSSModel.Advert })
            assertTrue(item.any { it is RSSModel.Message && it.msg.split(":").size == 3})
        }
    }

    @Test
    fun `init all sources disabled if excludes list contains all news sources`() = runTest {

        every { mockRssService.getNews() } returns mockResponse500
        every { mockRssRepository.rssUrls } returns emptySet()

        val expected = listOf<RSSModel>(
            RSSModel.SourcesDisabled
        )

        initUnderTest()
        advanceUntilIdle()

        underTest.outputs.list.test {
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `init internal error is thrown if results are empty`() = runTest {

        every { mockRssService.getNews() } returns mockResponse500

        val expected = listOf<RSSModel>(
            RSSModel.InternalError
        )

        initUnderTest()
        advanceUntilIdle()

        underTest.outputs.list.test {
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `no network error shown when network response code is no network`() = runTest {

        every { mockRssService.getNews() } returns mockResponseNoNetwork

        val expected = listOf<RSSModel>(
            RSSModel.NoNetwork
        )

        initUnderTest()
        advanceUntilIdle()

        underTest.outputs.list.test {
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `no network error shown when network connectivity check returns false`() = runTest {

        every { mockConnectivityManager.isConnected } returns false

        val expected = listOf<RSSModel>(
            RSSModel.NoNetwork
        )

        initUnderTest()
        advanceUntilIdle()

        underTest.outputs.list.test {
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `click configure opens rss settings`() = runTest {

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
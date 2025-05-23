package tmg.flashback.rss.presentation.feed

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.ads.ads.repository.model.AdvertConfig
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.device.managers.TimeManager
import tmg.flashback.navigation.Navigator
import tmg.flashback.rss.network.RssService
import tmg.flashback.rss.repo.RssRepository
import tmg.flashback.rss.repo.model.Article
import tmg.flashback.rss.repo.model.ArticleSource
import tmg.flashback.rss.repo.model.Response
import tmg.flashback.web.repository.WebBrowserRepository
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.testutils.BaseTest

internal class RSSViewModelTest: BaseTest() {

    private lateinit var underTest: RSSViewModel

    private val mockRssService: RssService = mockk(relaxed = true)
    private val mockRssRepository: RssRepository = mockk(relaxed = true)
    private val mockAdsRepository: AdsRepository = mockk(relaxed = true)
    private val mockWebBrowserRepository: WebBrowserRepository = mockk(relaxed = true)
    private val mockOpenWebpageUseCase: OpenWebpageUseCase = mockk(relaxed = true)
    private val mockConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)
    private val mockTimeManager: TimeManager = mockk(relaxed = true)

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

    private val mockResponse200 = Response<List<Article>>(listOf(mockArticle))
    private val mockResponseNoNetwork = Response<List<Article>>(null, -1)

    @BeforeEach
    internal fun setUp() {

        every { mockConnectivityManager.isConnected } returns true
        every { mockRssRepository.rssUrls } returns setOf("https://www.mock.rss.url.com")
        every { mockRssRepository.rssShowDescription } returns true
        coEvery { mockRssService.getNews() } returns mockResponse200
        every { mockAdsRepository.advertConfig } returns AdvertConfig(onRss = true)
        every { mockTimeManager.now } returns mockLocalDate
    }

    private fun initUnderTest() {

        underTest = RSSViewModel(
            rssService = mockRssService,
            rssRepository = mockRssRepository,
            adsRepository = mockAdsRepository,
            openWebpageUseCase = mockOpenWebpageUseCase,
            browserRepository = mockWebBrowserRepository,
            connectivityManager = mockConnectivityManager,
            timeManager = mockTimeManager
        )
        underTest.refresh()
    }

    @Test
    fun `initial state is set to no network when network is not connected`() = runTest(testDispatcher) {
        every { mockConnectivityManager.isConnected } returns false

        initUnderTest()
        underTest.uiState.test {
            assertEquals(RSSViewModel.UiState.NoNetwork, awaitItem())
        }
    }

    @Test
    fun `initial state is set to sources disables if rss repository has no sources`() = runTest(testDispatcher) {
        every { mockRssRepository.rssUrls } returns emptySet()

        initUnderTest()
        underTest.uiState.test {
            assertEquals(false, (awaitItem() as RSSViewModel.UiState.Data).hasSources)
        }
    }

    @Test
    fun `state is set to no network when responses have no network flag`() = runTest(testDispatcher) {
        coEvery { mockRssService.getNews() } returns mockResponseNoNetwork
        initUnderTest()
        underTest.uiState.test {
            assertEquals(RSSViewModel.UiState.NoNetwork, awaitItem())
        }
    }

    @Test
    fun `state is set to data with show advert config and rss list items`() = runTest(testDispatcher) {
        initUnderTest()
        underTest.uiState.test {
            assertEquals(RSSViewModel.UiState.Data(
                lastUpdated = "01:02:03",
                showAdvert = true,
                hasSources = true,
                rssItems = listOf(mockArticle)
            ), awaitItem())
        }
    }

    @Test
    fun `state is set to no network when refreshed but no network connection`() = runTest(testDispatcher) {
        initUnderTest()
        underTest.uiState.test {
            assertEquals(RSSViewModel.UiState.Data(
                lastUpdated = "01:02:03",
                showAdvert = true,
                hasSources = true,
                rssItems = listOf(mockArticle)
            ), awaitItem())
        }

        coEvery { mockRssService.getNews() } returns mockResponseNoNetwork
        underTest.refresh()
        underTest.uiState.test {
            assertEquals(RSSViewModel.UiState.NoNetwork, awaitItem())
        }
    }

    @Test
    fun `state is set to sources disabled when refreshed but to have no rss sources`() = runTest(testDispatcher) {
        initUnderTest()
        underTest.uiState.test {
            assertEquals(RSSViewModel.UiState.Data(
                lastUpdated = "01:02:03",
                showAdvert = true,
                hasSources = true,
                rssItems = listOf(mockArticle)
            ), awaitItem())
        }

        every { mockRssRepository.rssUrls } returns emptySet()
        underTest.refresh()
        underTest.uiState.test {
            assertEquals(false, (awaitItem() as RSSViewModel.UiState.Data).hasSources)
        }
    }

    @Test
    fun `state is set to data with new rss feeds when refreshed`() = runTest(testDispatcher) {
        coEvery { mockRssService.getNews() } returns mockResponseNoNetwork
        initUnderTest()
        underTest.uiState.test {
            assertEquals(RSSViewModel.UiState.NoNetwork, awaitItem())
        }

        coEvery { mockRssService.getNews() } returns mockResponse200
        underTest.refresh()
        underTest.uiState.test {
            assertEquals(RSSViewModel.UiState.Data(
                lastUpdated = "01:02:03",
                showAdvert = true,
                hasSources = true,
                rssItems = listOf(mockArticle)
            ), awaitItem())
        }
    }

    @Test
    fun `clicking article then going back`() = runTest(testDispatcher) {
        initUnderTest()
        underTest.uiState.test {
            assertEquals(RSSViewModel.UiState.Data(
                lastUpdated = "01:02:03",
                showAdvert = true,
                hasSources = true,
                rssItems = listOf(mockArticle)
            ), awaitItem())

            underTest.clickArticle(mockArticle)
            assertEquals(RSSViewModel.UiStateOpened.WebArticle(mockArticle), (awaitItem() as RSSViewModel.UiState.Data).opened)

            underTest.back()
            assertEquals(null, (awaitItem() as RSSViewModel.UiState.Data).opened)
        }
    }

    @Test
    fun `clicking configure sources then going back`() = runTest(testDispatcher) {
        initUnderTest()
        underTest.uiState.test {
            assertEquals(RSSViewModel.UiState.Data(
                lastUpdated = "01:02:03",
                showAdvert = true,
                hasSources = true,
                rssItems = listOf(mockArticle)
            ), awaitItem())
            coVerify(exactly = 2) {
                mockRssService.getNews()
            }

            underTest.configure()
            assertEquals(RSSViewModel.UiStateOpened.ConfigureSources, (awaitItem() as RSSViewModel.UiState.Data).opened)

            underTest.back()
            assertEquals(null, (awaitItem() as RSSViewModel.UiState.Data).opened)
            coVerify(exactly = 3) {
                mockRssService.getNews()
            }
        }
    }


    @Test
    fun `clicking article with open in external browser opens in external browser`() = runTest(testDispatcher) {
        every { mockWebBrowserRepository.openInExternal } returns true
        initUnderTest()
        underTest.uiState.test {
            assertEquals(RSSViewModel.UiState.Data(
                lastUpdated = "01:02:03",
                showAdvert = true,
                hasSources = true,
                rssItems = listOf(mockArticle)
            ), awaitItem())

            underTest.clickArticle(mockArticle)

            verify {
                mockOpenWebpageUseCase.open(mockArticle.link, mockArticle.title, true)
            }
        }
    }

    @Test
    fun `clicking article sets selected article in data`() = runTest(testDispatcher) {
        initUnderTest()
        underTest.uiState.test {
            assertEquals(RSSViewModel.UiState.Data(
                lastUpdated = "01:02:03",
                showAdvert = true,
                hasSources = true,
                rssItems = listOf(mockArticle)
            ), awaitItem())
        }

        underTest.clickArticle(mockArticle)
        underTest.uiState.test {
            assertEquals(RSSViewModel.UiState.Data(
                lastUpdated = "01:02:03",
                showAdvert = true,
                hasSources = true,
                rssItems = listOf(mockArticle),
                opened = RSSViewModel.UiStateOpened.WebArticle(mockArticle)
            ), awaitItem())
        }
    }
}
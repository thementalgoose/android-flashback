package tmg.flashback.rss.ui.feed

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDateTime
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
import tmg.testutils.BaseTest

internal class RSSViewModelTest: BaseTest() {

    private lateinit var underTest: RSSViewModel

    private val mockRssService: RssService = mockk(relaxed = true)
    private val mockRssRepository: RssRepository = mockk(relaxed = true)
    private val mockAdsRepository: AdsRepository = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)
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
            navigator = mockNavigator,
            connectivityManager = mockConnectivityManager,
            timeManager = mockTimeManager,
            openWebpageUseCase = mockk(relaxed = true)
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
            assertEquals(RSSViewModel.UiState.SourcesDisabled, awaitItem())
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
                rssItems = listOf(mockArticle)
            ), awaitItem())
        }

        every { mockRssRepository.rssUrls } returns emptySet()
        underTest.refresh()
        underTest.uiState.test {
            assertEquals(RSSViewModel.UiState.SourcesDisabled, awaitItem())
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
                rssItems = listOf(mockArticle)
            ), awaitItem())
        }
    }

    @Test
    fun `clicking article sets selected article in data`() = runTest(testDispatcher) {
        initUnderTest()
        underTest.uiState.test {
            assertEquals(RSSViewModel.UiState.Data(
                lastUpdated = "01:02:03",
                showAdvert = true,
                rssItems = listOf(mockArticle)
            ), awaitItem())
        }

        underTest.clickArticle(mockArticle)
        underTest.uiState.test {
            assertEquals(RSSViewModel.UiState.Data(
                lastUpdated = "01:02:03",
                showAdvert = true,
                rssItems = listOf(mockArticle),
                articleSelected = mockArticle
            ), awaitItem())
        }
    }
}
package tmg.flashback.season.presentation.messaging

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.flashbacknews.api.models.news.News
import tmg.flashback.flashbacknews.api.usecases.GetNewsUseCase
import tmg.flashback.season.repository.HomeRepository
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.testutils.BaseTest

internal class NewsViewModelTest: BaseTest() {

    private val mockGetNewsUseCase: GetNewsUseCase = mockk(relaxed = true)
    private val mockOpenWebpageUseCase: OpenWebpageUseCase = mockk(relaxed = true)
    private val mockHomeRepository: HomeRepository = mockk(relaxed = true)
    private val mockNetworkConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)

    private lateinit var underTest: NewsViewModel

    private fun initUnderTest() {
        underTest = NewsViewModel(
            getNewsUseCase = mockGetNewsUseCase,
            openWebpageUseCase = mockOpenWebpageUseCase,
            networkConnectivityManager = mockNetworkConnectivityManager,
            homeRepository = mockHomeRepository,
            ioDispatcher = testDispatcher
        )
    }

    @BeforeEach
    fun setUp() {
        every { mockHomeRepository.recentHighlights } returns true
        every { mockNetworkConnectivityManager.isConnected } returns true
    }

    @Test
    fun `initialising view model makes request`() {
        initUnderTest()
        coVerify {
            mockGetNewsUseCase.getNews()
        }
    }

    @Test
    fun `state updated from loading to news when successful response with items returned`() = runTest(testDispatcher) {
        coEvery { mockGetNewsUseCase.getNews() } returns emptyList()
        initUnderTest()

        underTest.outputs.uiState.test {
            assertEquals(NewsUiState.NoNews, awaitItem())
        }
    }

    @Test
    fun `state updated from loading to no news when successful response with no items`() = runTest(testDispatcher) {
        coEvery { mockGetNewsUseCase.getNews() } returns listOf(fakeNews())
        initUnderTest()

        underTest.outputs.uiState.test {
            assertEquals(NewsUiState.News(listOf(fakeDate to listOf(fakeNews()))), awaitItem())
        }
    }

    @Test
    fun `state stays no news if toggle disabled`() = runTest(testDispatcher) {
        every { mockHomeRepository.recentHighlights } returns false
        coEvery { mockGetNewsUseCase.getNews() } returns listOf(fakeNews())
        initUnderTest()

        underTest.outputs.uiState.test {
            assertEquals(NewsUiState.NoNews, awaitItem())

            underTest.refresh(false)

            coVerify(exactly = 0) { mockGetNewsUseCase.getNews() }
        }
    }

    @Test
    fun `refresh with background true does not show loading state`() = runTest(testDispatcher) {
        coEvery { mockGetNewsUseCase.getNews() } returns listOf(fakeNews())
        initUnderTest()

        underTest.outputs.uiState.test {
            assertEquals(NewsUiState.News(listOf(fakeDate to listOf(fakeNews()))), awaitItem())

            coEvery { mockGetNewsUseCase.getNews() } returns listOf(fakeNews(), fakeNews())
            underTest.refresh(true)
            advanceTimeBy(100L)

            assertEquals(NewsUiState.News(listOf(fakeDate to listOf(fakeNews(), fakeNews()))), awaitItem())
        }
    }

    @Test
    fun `refresh with background false shows loading state`() = runTest(testDispatcher) {
        coEvery { mockGetNewsUseCase.getNews() } returns listOf(fakeNews())
        initUnderTest()

        underTest.outputs.uiState.test {
            assertEquals(NewsUiState.News(listOf(fakeDate to listOf(fakeNews()))), awaitItem())

            coEvery { mockGetNewsUseCase.getNews() } returns listOf(fakeNews(), fakeNews())
            underTest.refresh(false)
            advanceTimeBy(100L)

            assertEquals(NewsUiState.News(listOf(fakeDate to listOf(fakeNews(), fakeNews()))), awaitItem())
        }
    }

    @Test
    fun `clicking item calls open webpage use case`() {
        initUnderTest()
        underTest.itemClicked("url")
        verify {
            mockOpenWebpageUseCase.open("url", "")
        }
    }

    @Test
    fun `clicking hide sets state to no news calls open webpage use case`() = runTest(testDispatcher) {
        coEvery { mockGetNewsUseCase.getNews() } returns listOf(fakeNews())
        initUnderTest()

        underTest.outputs.uiState.test {
            assertEquals(NewsUiState.News(listOf(fakeDate to listOf(fakeNews()))), awaitItem())

            coEvery { mockGetNewsUseCase.getNews() } returns listOf(fakeNews(), fakeNews())
            underTest.refresh(false)
            advanceTimeBy(100L)

            assertEquals(NewsUiState.News(listOf(fakeDate to listOf(fakeNews(), fakeNews()))), awaitItem())

            underTest.hide()

            assertEquals(NewsUiState.NoNews, awaitItem())
        }
    }

    private val fakeDate = LocalDate.of(2024, 2, 2)
    private fun fakeNews(
        message: String = "Message",
        url: String? = "https://www.url.com",
        dateAdded: String = "2024-02-02",
        image: String? = null
    ): News = News(
        message = message,
        url = url,
        image = image,
        dateAdded = dateAdded
    )
}
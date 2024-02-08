package tmg.flashback.season.presentation.messaging

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import tmg.flashback.flashbacknews.api.models.news.News
import tmg.flashback.flashbacknews.api.usecases.GetNewsUseCase

internal class NewsViewModelTest {

    private val mockGetNewsUseCase: GetNewsUseCase = mockk(relaxed = true)

    private lateinit var underTest: NewsViewModel

    private fun initUnderTest() {
        underTest = NewsViewModel(
            getNewsUseCase = mockGetNewsUseCase,
            ioDispatcher = Dispatchers.Unconfined
        )
    }

    @Test
    fun `initialising view model makes request`() {
        initUnderTest()
        coVerify {
            mockGetNewsUseCase.getNews()
        }
    }

    @Test
    fun `state updated from loading to news when successful response with items returned`() = runTest {
        coEvery { mockGetNewsUseCase.getNews() } returns emptyList()
        initUnderTest()

        underTest.outputs.uiState.test {
            assertEquals(NewsUiState.NoNews, awaitItem())
        }
    }

    @Test
    fun `state updated from loading to no news when successful response with no items`() = runTest {
        coEvery { mockGetNewsUseCase.getNews() } returns listOf(fakeNews())
        initUnderTest()

        underTest.outputs.uiState.test {
            assertEquals(NewsUiState.News(listOf(fakeNews())), awaitItem())
        }
    }

    @Test
    fun `refresh with background true does not show loading state`() = runTest {
        coEvery { mockGetNewsUseCase.getNews() } returns listOf(fakeNews())
        initUnderTest()

        underTest.outputs.uiState.test {
            assertEquals(NewsUiState.News(listOf(fakeNews())), awaitItem())

            underTest.refresh(true)

            assertEquals(NewsUiState.Loading, awaitItem())
            assertEquals(NewsUiState.News(listOf(fakeNews())), awaitItem())
        }
    }

    @Test
    fun `refresh with background false shows loading state`() = runTest {
        coEvery { mockGetNewsUseCase.getNews() } returns listOf(fakeNews())
        initUnderTest()

        underTest.outputs.uiState.test {
            assertEquals(NewsUiState.News(listOf(fakeNews())), awaitItem())

            underTest.refresh(false)

            assertEquals(NewsUiState.News(listOf(fakeNews())), awaitItem())
        }
    }

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
package tmg.flashback.flashbacknews.api.usecases

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import retrofit2.Response
import tmg.flashback.flashbacknews.api.api.FlashbackNewsApi
import tmg.flashback.flashbacknews.api.models.MetadataWrapper
import tmg.flashback.flashbacknews.api.models.news.News
import java.lang.Exception

internal class GetNewsUseCaseImplTest {

    private val mockFlashbackNewsApi: FlashbackNewsApi = mockk(relaxed = true)

    private lateinit var underTest: GetNewsUseCaseImpl

    private fun initUnderTest() {
        underTest = GetNewsUseCaseImpl(
            flashbackNewsApi = mockFlashbackNewsApi
        )
    }

    @Test
    fun `get news returns list of news if successful`() = runTest {
        coEvery { mockFlashbackNewsApi.getNews() } returns Response.success(MetadataWrapper(1, listOf(fakeNews())))

        initUnderTest()
        val result = underTest.getNews()

        assertEquals(listOf(fakeNews()), result)
    }

    @Test
    fun `get news returns empty list if successful with empty`() = runTest {
        coEvery { mockFlashbackNewsApi.getNews() } returns Response.success(MetadataWrapper(1, emptyList()))

        initUnderTest()
        val result = underTest.getNews()

        assertEquals(emptyList<News>(), result)
    }

    @Test
    fun `get news returns null when exception is thrown`() = runTest {
        coEvery { mockFlashbackNewsApi.getNews() } throws Exception("Help")

        initUnderTest()
        val result = underTest.getNews()

        assertEquals(null, result)
    }


    @Test
    fun `get news returns null when response is error`() = runTest {
        coEvery { mockFlashbackNewsApi.getNews() } returns Response.error(404, "".toResponseBody())

        initUnderTest()
        val result = underTest.getNews()

        assertEquals(null, result)
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
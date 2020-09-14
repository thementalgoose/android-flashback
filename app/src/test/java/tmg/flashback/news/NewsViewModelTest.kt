package tmg.flashback.news

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.verify
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
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.db.news.NewsDB
import tmg.flashback.repo.enums.NewsSource
import tmg.flashback.repo.models.Response
import tmg.flashback.repo.models.news.Article
import tmg.flashback.repo.models.news.ArticleSource
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.shared.SyncDataItem
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertValue
import tmg.flashback.testutils.assertValues
import tmg.flashback.testutils.test

@FlowPreview
@ExperimentalCoroutinesApi
class NewsViewModelTest: BaseTest() {

    private lateinit var sut: NewsViewModel

    private val mockNewsDB: NewsDB = mock()
    private val mockPrefsDB: PrefsDB = mock()
    private val mockConnectivityManager: ConnectivityManager = mock()

    private val mockLocalDate: LocalDateTime = LocalDateTime.now()
    private val mockArticleSource = ArticleSource(
        source = "Test source",
        colour = "#123456",
        sourceShort = "short something here",
        link = "https://www.google.com/source"
    )
    private val mockArticle = Article(
        id = "test",
        title = "test title",
        description = "test description",
        showDescription = false,
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
        whenever(mockPrefsDB.newsSourceExcludeList).thenReturn(emptySet())
        whenever(mockPrefsDB.newsShowDescription).thenReturn(true)
        whenever(mockNewsDB.getNews()).thenReturn(mockResponse200)
    }

    private fun initSUT() {

        sut = NewsViewModel(mockNewsDB, mockPrefsDB, mockConnectivityManager, testScopeProvider)
    }

    @Test
    fun `NewsViewModel is refreshing is initialised is reset after refresh flow`() = coroutineTest {

        val expectedObservedList = listOf(
                NewsItem.Message(mockLocalDate.format(DateTimeFormatter.ofPattern("HH:mm:ss"))),
                NewsItem.News(mockArticle)
        )

        initSUT()

        val observer = sut.outputs.isRefreshing.test()
        advanceUntilIdle()

        assertValue(expectedObservedList, sut.outputs.list)
        assertEquals(listOf(true, false), observer.listOfValues.toList())

        sut.inputs.refresh()

        advanceUntilIdle()

        assertEquals(listOf(true, false, true, false), observer.listOfValues.toList())
    }

    @Test
    fun `NewsViewModel init loads all news sources`() = coroutineTest {

        val expected = listOf(
            NewsItem.Message(mockLocalDate.format(DateTimeFormatter.ofPattern("HH:mm:ss"))),
            NewsItem.News(mockArticle)
        )

        initSUT()
        advanceUntilIdle()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `NewsViewModel init all sources disabled if excludes list contains all news sources`() = coroutineTest {

        whenever(mockNewsDB.getNews()).thenReturn(mockResponse500)
        whenever(mockPrefsDB.newsSourceExcludeList).thenReturn(NewsSource.values().toSet())

        val expected = listOf<NewsItem>(
            NewsItem.ErrorItem(SyncDataItem.AllSourcesDisabled)
        )

        initSUT()
        advanceUntilIdle()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `NewsViewModel init internal error is thrown if results are empty`() = coroutineTest {

        whenever(mockNewsDB.getNews()).thenReturn(mockResponse500)

        val expected = listOf<NewsItem>(
            NewsItem.ErrorItem(SyncDataItem.InternalError)
        )

        initSUT()
        advanceUntilIdle()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `NewsViewModel no network error shown when network response code is no network`() = coroutineTest {

        whenever(mockNewsDB.getNews()).thenReturn(mockResponseNoNetwork)

        val expected = listOf<NewsItem>(
            NewsItem.ErrorItem(SyncDataItem.NoNetwork)
        )

        initSUT()
        advanceUntilIdle()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `NewsViewModel no network error shown when network connectivity check returns false`() = coroutineTest {

        whenever(mockConnectivityManager.isConnected).thenReturn(false)

        val expected = listOf<NewsItem>(
            NewsItem.ErrorItem(SyncDataItem.NoNetwork)
        )

        initSUT()
        advanceUntilIdle()

        assertValue(expected, sut.outputs.list)
    }


    @AfterEach
    internal fun tearDown() {

        reset(mockNewsDB, mockPrefsDB, mockConnectivityManager)
    }
}
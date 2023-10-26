package tmg.flashback.rss.presentation.configure

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.flashback.rss.repo.RssRepository
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.rss.usecases.AllSupportedSourcesUseCase
import tmg.flashback.rss.usecases.GetSupportedSourceUseCase
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.testutils.BaseTest

internal class ConfigureRSSViewModelTest: BaseTest() {

    private val mockRssRepository: RssRepository = mockk(relaxed = true)
    private val mockAllSupportedSourcesUseCase: AllSupportedSourcesUseCase = mockk(relaxed = true)
    private val mockGetSupportedSourceUseCase: GetSupportedSourceUseCase = mockk(relaxed = true)
    private val mockOpenWebpageUseCase: OpenWebpageUseCase = mockk(relaxed = true)

    private lateinit var underTest: ConfigureRSSViewModel

    private fun initUnderTest() {
        underTest = ConfigureRSSViewModel(
            repository = mockRssRepository,
            allSupportedSourcesUseCase = mockAllSupportedSourcesUseCase,
            getSupportedSourcesUseCase = mockGetSupportedSourceUseCase,
            openWebpageUseCase = mockOpenWebpageUseCase
        )
    }

    @Test
    fun `show add custom is enabled when config is true`() = runTest(testDispatcher) {
        every { mockRssRepository.addCustom } returns true

        initUnderTest()
        underTest.outputs.showAddCustom.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `show add custom is disabled when config is false`() = runTest(testDispatcher) {
        every { mockRssRepository.addCustom } returns false

        initUnderTest()
        underTest.outputs.showAddCustom.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `show description is enabled when pref is true`() = runTest(testDispatcher) {
        every { mockRssRepository.rssShowDescription } returns true

        initUnderTest()
        underTest.outputs.showDescriptionEnabled.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `show description is disabled when pref is false`() = runTest(testDispatcher) {
        every { mockRssRepository.rssShowDescription } returns false

        initUnderTest()
        underTest.outputs.showDescriptionEnabled.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `clicking show description updates pref and updates value`() = runTest(testDispatcher) {
        every { mockRssRepository.rssShowDescription } returns false

        initUnderTest()
        underTest.outputs.showDescriptionEnabled.test {
            assertEquals(false, awaitItem())
        }
        underTest.inputs.clickShowDescription(true)

        underTest.outputs.showDescriptionEnabled.test {
            assertEquals(true, awaitItem())
        }
        verify {
            mockRssRepository.rssShowDescription = true
        }
    }

    @Test
    fun `rss list is emitted with supported and custom sources`() = runTest(testDispatcher) {
        every { mockRssRepository.rssUrls } returns setOf(
            fakeSupportedArticleSource.rssLink,
            "https://www.custom_rss.com/rss"
        )
        every { mockAllSupportedSourcesUseCase.getSources() } returns listOf(fakeSupportedArticleSource)

        initUnderTest()
        underTest.outputs.rssSources.test {
            val item = awaitItem()
            assertTrue(item.any { it.isChecked && it.url == "https://www.custom_rss.com/rss" })
            assertTrue(item.any { it.isChecked && it.url == fakeSupportedArticleSource.rssLink && it.supportedArticleSource == fakeSupportedArticleSource })
        }
    }

    @Test
    fun `adding custom source updates rss list`() = runTest(testDispatcher) {
        every { mockRssRepository.rssUrls } returns setOf(
            fakeSupportedArticleSource.rssLink
        )
        every { mockAllSupportedSourcesUseCase.getSources() } returns listOf(fakeSupportedArticleSource)

        initUnderTest()

        every { mockRssRepository.rssUrls } returns setOf(
            "https://www.custom_rss.com/rss",
            fakeSupportedArticleSource.rssLink
        )
        underTest.inputs.addItem("https://www.custom_rss.com/rss", isChecked = true)

        underTest.outputs.rssSources.test {
            val item = awaitItem()
            assertTrue(item.any { it.isChecked && it.url == "https://www.custom_rss.com/rss" })
            assertTrue(item.any { it.isChecked && it.url == fakeSupportedArticleSource.rssLink && it.supportedArticleSource == fakeSupportedArticleSource })
        }
    }

    @Test
    fun `removing custom source updates rss list`() = runTest(testDispatcher) {
        every { mockRssRepository.rssUrls } returns setOf(
            fakeSupportedArticleSource.rssLink,
            "https://www.custom_rss.com/rss"
        )
        every { mockAllSupportedSourcesUseCase.getSources() } returns listOf(fakeSupportedArticleSource)

        initUnderTest()

        every { mockRssRepository.rssUrls } returns setOf(
            fakeSupportedArticleSource.rssLink
        )
        underTest.inputs.addItem("https://www.custom_rss.com/rss", isChecked = false)

        underTest.outputs.rssSources.test {
            val item = awaitItem()
            assertTrue(item.none { it.isChecked && it.url == "https://www.custom_rss.com/rss"})
            assertTrue(item.any { it.isChecked && it.url == fakeSupportedArticleSource.rssLink && it.supportedArticleSource == fakeSupportedArticleSource })
        }
    }

    @Test
    fun `adding supported source updates rss list`() = runTest(testDispatcher) {
        every { mockRssRepository.rssUrls } returns setOf(
            "https://www.custom_rss.com/rss"
        )
        every { mockAllSupportedSourcesUseCase.getSources() } returns listOf(fakeSupportedArticleSource)

        initUnderTest()
        underTest.outputs.rssSources.test {
            val item = awaitItem()
            assertTrue(item.any { it.isChecked && it.url == "https://www.custom_rss.com/rss" })
            assertTrue(item.any { !it.isChecked && it.url == fakeSupportedArticleSource.rssLink && it.supportedArticleSource == fakeSupportedArticleSource })
        }

        every { mockRssRepository.rssUrls } returns setOf(
            "https://www.custom_rss.com/rss",
            fakeSupportedArticleSource.rssLink
        )
        underTest.inputs.addItem(fakeSupportedArticleSource.rssLink, isChecked = true)

        underTest.outputs.rssSources.test {
            val item = awaitItem()
            assertTrue(item.any { it.isChecked && it.url == "https://www.custom_rss.com/rss" })
            assertTrue(item.any { it.isChecked && it.url == fakeSupportedArticleSource.rssLink })
        }
    }

    @Test
    fun `removing supported source updates rss list`() = runTest(testDispatcher) {
        every { mockRssRepository.rssUrls } returns setOf(
            "https://www.custom_rss.com/rss",
            fakeSupportedArticleSource.rssLink
        )
        every { mockAllSupportedSourcesUseCase.getSources() } returns listOf(fakeSupportedArticleSource)

        initUnderTest()
        underTest.outputs.rssSources.test {
            val item = awaitItem()
            assertTrue(item.any { it.url == "https://www.custom_rss.com/rss" })
            assertTrue(item.any { it.isChecked && it.url == fakeSupportedArticleSource.rssLink && it.supportedArticleSource == fakeSupportedArticleSource })
        }

        every { mockRssRepository.rssUrls } returns setOf(
            "https://www.custom_rss.com/rss"
        )
        underTest.inputs.addItem(fakeSupportedArticleSource.rssLink, isChecked = false)


        underTest.outputs.rssSources.test {
            val item = awaitItem()
            assertTrue(item.any { it.url == "https://www.custom_rss.com/rss" })
            assertTrue(item.any { !it.isChecked && it.url == fakeSupportedArticleSource.rssLink && it.supportedArticleSource == fakeSupportedArticleSource })
        }
    }

    @Test
    fun `visit website forwards call to website navigator`() = runTest(testDispatcher) {
        initUnderTest()
        underTest.inputs.visitWebsite(fakeSupportedArticleSource)

        verify {
            mockOpenWebpageUseCase.open(fakeSupportedArticleSource.contactLink, title = "")
        }
    }
}



private val fakeSupportedArticleSource: SupportedArticleSource = SupportedArticleSource(
    rssLink = "https://www.url.com/f1/rss.xml",
    sourceShort = "URL",
    source = "https://www.url.com/",
    colour = "#928284",
    textColour = "#efefef",
    title = "Motorsport API",
    contactLink = "https://www.url.com/contact",
)
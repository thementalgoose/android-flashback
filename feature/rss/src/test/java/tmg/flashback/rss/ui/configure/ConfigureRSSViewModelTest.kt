package tmg.flashback.rss.ui.configure

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.rss.usecases.AllSupportedSourcesUseCase
import tmg.flashback.rss.usecases.GetSupportedSourceUseCase
import tmg.flashback.web.WebNavigationComponent
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertListDoesNotMatchItem
import tmg.testutils.livedata.assertListMatchesItem
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve

internal class ConfigureRSSViewModelTest: BaseTest() {

    private val mockRssRepository: RSSRepository = mockk(relaxed = true)
    private val mockAllSupportedSourcesUseCase: AllSupportedSourcesUseCase = mockk(relaxed = true)
    private val mockGetSupportedSourceUseCase: GetSupportedSourceUseCase = mockk(relaxed = true)
    private val mockWebNavigationComponent: WebNavigationComponent = mockk(relaxed = true)

    private lateinit var underTest: ConfigureRSSViewModel

    private fun initUnderTest() {
        underTest = ConfigureRSSViewModel(
            repository = mockRssRepository,
            allSupportedSourcesUseCase = mockAllSupportedSourcesUseCase,
            getSupportedSourcesUseCase = mockGetSupportedSourceUseCase,
            webNavigationComponent = mockWebNavigationComponent
        )
    }

    @Test
    fun `show add custom is enabled when config is true`() = coroutineTest {
        every { mockRssRepository.addCustom } returns true

        initUnderTest()
        underTest.outputs.showAddCustom.test {
            assertValue(true)
        }
    }

    @Test
    fun `show add custom is disabled when config is false`() = coroutineTest {
        every { mockRssRepository.addCustom } returns false

        initUnderTest()
        underTest.outputs.showAddCustom.test {
            assertValue(false)
        }
    }

    @Test
    fun `show description is enabled when pref is true`() = coroutineTest {
        every { mockRssRepository.rssShowDescription } returns true

        initUnderTest()
        underTest.outputs.showDescriptionEnabled.test {
            assertValue(true)
        }
    }

    @Test
    fun `show description is disabled when pref is false`() = coroutineTest {
        every { mockRssRepository.rssShowDescription } returns false

        initUnderTest()
        underTest.outputs.showDescriptionEnabled.test {
            assertValue(false)
        }
    }

    @Test
    fun `clicking show description updates pref and updates value`() = coroutineTest {
        every { mockRssRepository.rssShowDescription } returns false

        initUnderTest()
        val observer = underTest.outputs.showDescriptionEnabled.testObserve()
        underTest.inputs.clickShowDescription(true)

        verify {
            mockRssRepository.rssShowDescription = true
        }
        observer.assertEmittedCount(2)
    }

    @Test
    fun `rss list is emitted with supported and custom sources`() = coroutineTest {
        every { mockRssRepository.rssUrls } returns setOf(
            fakeSupportedArticleSource.rssLink,
            "https://www.custom_rss.com/rss"
        )
        every { mockAllSupportedSourcesUseCase.getSources() } returns listOf(fakeSupportedArticleSource)

        initUnderTest()
        underTest.outputs.rssSources.test {
            assertListMatchesItem { it.isChecked && it.url == "https://www.custom_rss.com/rss" }
            assertListMatchesItem { it.isChecked && it.url == fakeSupportedArticleSource.rssLink && it.supportedArticleSource == fakeSupportedArticleSource }
        }
    }

    @Test
    fun `adding custom source updates rss list`() = coroutineTest {
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
            assertListMatchesItem { it.isChecked && it.url == "https://www.custom_rss.com/rss" }
            assertListMatchesItem { it.isChecked && it.url == fakeSupportedArticleSource.rssLink && it.supportedArticleSource == fakeSupportedArticleSource }
        }
    }

    @Test
    fun `removing custom source updates rss list`() = coroutineTest {
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
            assertListDoesNotMatchItem { it.isChecked && it.url == "https://www.custom_rss.com/rss"}
            assertListMatchesItem { it.isChecked && it.url == fakeSupportedArticleSource.rssLink && it.supportedArticleSource == fakeSupportedArticleSource }
        }
    }

    @Test
    fun `adding supported source updates rss list`() = coroutineTest {
        every { mockRssRepository.rssUrls } returns setOf(
            "https://www.custom_rss.com/rss"
        )
        every { mockAllSupportedSourcesUseCase.getSources() } returns listOf(fakeSupportedArticleSource)

        initUnderTest()
        underTest.outputs.rssSources.test {
            assertListMatchesItem { it.isChecked && it.url == "https://www.custom_rss.com/rss" }
            assertListMatchesItem { !it.isChecked && it.url == fakeSupportedArticleSource.rssLink && it.supportedArticleSource == fakeSupportedArticleSource }
        }

        every { mockRssRepository.rssUrls } returns setOf(
            "https://www.custom_rss.com/rss",
            fakeSupportedArticleSource.rssLink
        )
        underTest.inputs.addItem(fakeSupportedArticleSource.rssLink, isChecked = true)

        underTest.outputs.rssSources.test {
            assertListMatchesItem { it.isChecked && it.url == "https://www.custom_rss.com/rss" }
            assertListMatchesItem { it.isChecked && it.url == fakeSupportedArticleSource.rssLink }
        }
    }

    @Test
    fun `removing supported source updates rss list`() = coroutineTest {
        every { mockRssRepository.rssUrls } returns setOf(
            "https://www.custom_rss.com/rss",
            fakeSupportedArticleSource.rssLink
        )
        every { mockAllSupportedSourcesUseCase.getSources() } returns listOf(fakeSupportedArticleSource)

        initUnderTest()
        underTest.outputs.rssSources.test {
            assertListMatchesItem { it.url == "https://www.custom_rss.com/rss" }
            assertListMatchesItem { it.isChecked && it.url == fakeSupportedArticleSource.rssLink && it.supportedArticleSource == fakeSupportedArticleSource }
        }

        every { mockRssRepository.rssUrls } returns setOf(
            "https://www.custom_rss.com/rss"
        )
        underTest.inputs.addItem(fakeSupportedArticleSource.rssLink, isChecked = false)


        underTest.outputs.rssSources.test {
            assertListMatchesItem { it.url == "https://www.custom_rss.com/rss" }
            assertListMatchesItem { !it.isChecked && it.url == fakeSupportedArticleSource.rssLink && it.supportedArticleSource == fakeSupportedArticleSource }
        }
    }

    @Test
    fun `visit website forwards call to website navigator`() = coroutineTest {
        initUnderTest()
        underTest.inputs.visitWebsite(fakeSupportedArticleSource)

        verify {
            mockWebNavigationComponent.web(fakeSupportedArticleSource.contactLink)
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
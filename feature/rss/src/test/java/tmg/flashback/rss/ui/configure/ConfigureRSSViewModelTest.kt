package tmg.flashback.rss.ui.configure

import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.web.WebNavigationComponent
import tmg.testutils.BaseTest

internal class ConfigureRSSViewModelTest: BaseTest() {

    private val mockRssRepository: RSSRepository = mockk(relaxed = true)
    private val mockRssController: RSSController = mockk(relaxed = true)
    private val mockWebNavigationComponent: WebNavigationComponent = mockk(relaxed = true)

    private lateinit var underTest: ConfigureRSSViewModel

    private fun initUnderTest() {
        underTest = ConfigureRSSViewModel(
            repository = mockRssRepository,
            rssFeedController = mockRssController,
            webNavigationComponent = mockWebNavigationComponent
        )
    }
}
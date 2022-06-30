package tmg.flashback.rss.ui.settings

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.rss.RssNavigationComponent
import tmg.flashback.rss.R
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.testutils.assertExpectedOrder
import tmg.flashback.rss.testutils.findPref
import tmg.flashback.rss.testutils.findSwitch
import tmg.testutils.BaseTest

internal class SettingsRSSViewModelTest: BaseTest() {

    private val mockRssRepository: RSSRepository = mockk(relaxed = true)
    private val mockRssNavigationComponent: RssNavigationComponent = mockk(relaxed = true)

    lateinit var sut: SettingsRSSViewModel

    private fun initSUT() {
        sut = SettingsRSSViewModel(mockRssRepository, mockRssNavigationComponent)
    }

    @Test
    fun `initial model list is expected`() {
        initSUT()
        val expected = listOf(
                Pair(R.string.settings_rss_configure, null),
                Pair(R.string.settings_rss_configure_sources_title, R.string.settings_rss_configure_sources_description),
                Pair(R.string.settings_rss_appearance_title, null),
                Pair(R.string.settings_rss_show_description_title, R.string.settings_rss_show_description_description),
        )

        sut.models.assertExpectedOrder(expected)
    }

    @Test
    fun `clicking pref model for configure launches configure event`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_rss_configure_sources_title))
        verify {
            mockRssNavigationComponent.configureRSS()
        }
    }

    @Test
    fun `clicking pref model for show description updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_rss_show_description_title), true)
        verify {
            mockRssRepository.rssShowDescription = true
        }
    }
}
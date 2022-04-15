package tmg.flashback.rss.ui.settings.settings

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.rss.R
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.testutils.assertExpectedOrder
import tmg.flashback.rss.testutils.findPref
import tmg.flashback.rss.testutils.findSwitch
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class RSSSettingsViewModelTest: BaseTest() {

    private val mockRssRepository: RSSRepository = mockk(relaxed = true)

    lateinit var sut: RSSSettingsViewModel

    private fun initSUT() {
        sut = RSSSettingsViewModel(mockRssRepository)
    }

    @Test
    fun `initial model list is expected`() {
        initSUT()
        val expected = listOf(
                Pair(R.string.settings_rss_configure, null),
                Pair(R.string.settings_rss_configure_sources_title, R.string.settings_rss_configure_sources_description),
                Pair(R.string.settings_rss_appearance_title, null),
                Pair(R.string.settings_rss_show_description_title, R.string.settings_rss_show_description_description),
                Pair(R.string.settings_rss_browser, null),
                Pair(R.string.settings_rss_browser_external_title, R.string.settings_rss_browser_external_description),
                Pair(R.string.settings_rss_browser_javascript_title, R.string.settings_rss_browser_javascript_description),
        )

        sut.models.assertExpectedOrder(expected)
    }

    @Test
    fun `clicking pref model for configure launches configure event`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_rss_configure_sources_title))
        sut.outputs.goToConfigure.test {
            assertEventFired()
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

    @Test
    fun `clicking pref model for rss external browser updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_rss_browser_external_title), true)
        verify {
            mockRssRepository.newsOpenInExternalBrowser = true
        }
    }

    @Test
    fun `clicking pref model for broswer javascript updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_rss_browser_javascript_title), true)
        verify {
            mockRssRepository.inAppEnableJavascript = true
        }
    }
}
package tmg.flashback.rss.ui.settings.settings

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.core.ui.settings.SettingsModel
import tmg.flashback.rss.R
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.testutils.BaseTest
import tmg.flashback.rss.testutils.assertEventFired
import tmg.flashback.rss.testutils.test

class RSSSettingsViewModelTest: BaseTest() {

    private val mockRssRepository: RSSRepository = mockk(relaxed = true)

    lateinit var sut: RSSSettingsViewModel

    private fun initSUT() {
        sut = RSSSettingsViewModel(mockRssRepository)
    }

    @Test
    fun `initial model list is expected`() {
        initSUT()
        (sut.models[0] as SettingsModel.Header).apply {
            assertEquals(R.string.settings_rss_configure, this.title)
        }
        (sut.models[1] as SettingsModel.Pref).apply {
            assertEquals(R.string.settings_rss_configure_sources_title, this.title)
            assertEquals(R.string.settings_rss_configure_sources_description, this.description)
        }
        (sut.models[2] as SettingsModel.Header).apply {
            assertEquals(R.string.settings_rss_appearance_title, this.title)
        }
        (sut.models[3] as SettingsModel.Pref).apply {
            assertEquals(R.string.settings_rss_show_description_title, this.title)
            assertEquals(R.string.settings_rss_show_description_description, this.description)
        }
        (sut.models[4] as SettingsModel.Header).apply {
            assertEquals(R.string.settings_rss_browser, this.title)
        }
        (sut.models[5] as SettingsModel.Pref).apply {
            assertEquals(R.string.settings_rss_browser_external_title, this.title)
            assertEquals(R.string.settings_rss_browser_external_description, this.description)
        }
        (sut.models[6] as SettingsModel.Pref).apply {
            assertEquals(R.string.settings_rss_browser_javascript_title, this.title)
            assertEquals(R.string.settings_rss_browser_javascript_description, this.description)
        }
    }

    @Test
    fun `clicking pref model for configure launches configure event`() {
        initSUT()
        sut.clickPreference(sut.models[1] as SettingsModel.Pref)
        sut.outputs.goToConfigure.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking pref model for show description updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models[3] as SettingsModel.SwitchPref, true)
        verify {
            mockRssRepository.rssShowDescription = true
        }
    }

    @Test
    fun `clicking pref model for rss external browser updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models[5] as SettingsModel.SwitchPref, true)
        verify {
            mockRssRepository.newsOpenInExternalBrowser = true
        }
    }

    @Test
    fun `clicking pref model for broswer javascript updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models[6] as SettingsModel.SwitchPref, true)
        verify {
            mockRssRepository.inAppEnableJavascript = true
        }
    }
}
package tmg.flashback.rss.ui.settings.settings

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.rss.R
import tmg.flashback.rss.prefs.RSSRepository
import tmg.flashback.rss.testutils.BaseTest
import tmg.flashback.rss.testutils.assertEventFired
import tmg.flashback.rss.testutils.test
import tmg.flashback.rss.ui.settings.RSSSettingsViewModel

class RSSSettingsViewModelTest: BaseTest() {

    lateinit var sut: RSSSettingsViewModel

    private val mock: RSSRepository = mockk(relaxed = true)

    private val keyConfigureSources: String = "keyConfigureSources"
    private val keyShowDescription: String = "keyShowDescription"
    private val keyJavascript: String = "keyJavascript"
    private val keyOpenInExternalBrowser: String = "keyOpenInExternalBrowser"

    @BeforeEach
    internal fun setUp() {

        every { mock.rssShowDescription } returns false
        every { mock.inAppEnableJavascript } returns false
        every { mock.rssUrls } returns emptySet()

        sut = RSSSettingsViewModel(mock)
    }

    @Test
    fun `settings list is returned properly`() {

        val expected = listOf(
            AppPreferencesItem.Category(R.string.settings_rss_configure),
            AppPreferencesItem.Preference(
                keyConfigureSources,
                R.string.settings_rss_configure_sources_title,
                R.string.settings_rss_configure_sources_description
            ),
            AppPreferencesItem.Category(R.string.settings_rss_appearance_title),
            AppPreferencesItem.SwitchPreference(
                keyShowDescription,
                R.string.settings_rss_show_description_title,
                R.string.settings_rss_show_description_description,
                false
            ),
            AppPreferencesItem.Category(R.string.settings_rss_browser),
            AppPreferencesItem.SwitchPreference(
                keyOpenInExternalBrowser,
                R.string.settings_rss_browser_external_title,
                R.string.settings_rss_browser_external_description,
                false
            ),
            AppPreferencesItem.SwitchPreference(
                keyJavascript,
                R.string.settings_rss_browser_javascript_title,
                R.string.settings_rss_browser_javascript_description,
                false
            )
        )

        sut.outputs.settings.test {
            assertValue(expected)
        }
    }

    @Test
    fun `clicking configure sources item notifies navigation`() {

        sut.clickPref(keyConfigureSources)

        sut.outputs.goToConfigure.test {
            assertEventFired()
        }
    }

    @Test
    fun `update show description marks it enabled in prefs`() {

        sut.updatePref(keyShowDescription, true)

        verify { mock.rssShowDescription = true }
    }

    @Test
    fun `update enable javascript marks it enabled in prefs`() {

        sut.updatePref(keyJavascript, true)

        verify { mock.inAppEnableJavascript = true }
    }

    @Test
    fun `update enable open in external browser marks it enabled in prefs`() {

        sut.updatePref(keyOpenInExternalBrowser, true)

        verify { mock.newsOpenInExternalBrowser = true}
    }
}
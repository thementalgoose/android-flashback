package tmg.flashback.rss.ui.settings

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertEventFired
import tmg.flashback.testutils.test

class RSSSettingsViewModelTest: BaseTest() {

    lateinit var sut: RSSSettingsViewModel

    private val mockPrefs: PrefsDB = mock()

    private val keyConfigureSources: String = "keyConfigureSources"
    private val keyShowDescription: String = "keyShowDescription"
    private val keyJavascript: String = "keyJavascript"
    private val keyOpenInExternalBrowser: String = "keyOpenInExternalBrowser"

    @BeforeEach
    internal fun setUp() {

        whenever(mockPrefs.rssShowDescription).thenReturn(false)
        whenever(mockPrefs.inAppEnableJavascript).thenReturn(false)
        whenever(mockPrefs.rssUrls).thenReturn(emptySet())

        sut = RSSSettingsViewModel(
            mockPrefs,
            testScopeProvider
        )
    }

    @Test
    fun `RSSSettingsViewModel settings list is returned properly`() {

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

        assertEquals(expected, sut.outputs.settings.test().latestValue())
    }

    @Test
    fun `RSSSettingsViewModel clicking configure sources item notifies navigation`() {

        sut.clickPref(keyConfigureSources)

        assertEventFired(sut.outputs.goToConfigure)
    }

    @Test
    fun `RSSSettingsViewModel update show description marks it enabled in prefs`() {

        sut.updatePref(keyShowDescription, true)

        verify(mockPrefs).rssShowDescription = true
    }

    @Test
    fun `RSSSettingsViewModel update enable javascript marks it enabled in prefs`() {

        sut.updatePref(keyJavascript, true)

        verify(mockPrefs).inAppEnableJavascript = true
    }

    @Test
    fun `RSSSettingsViewModel update enable open in external browser marks it enabled in prefs`() {

        sut.updatePref(keyOpenInExternalBrowser, true)

        verify(mockPrefs).newsOpenInExternalBrowser = true
    }

    @AfterEach
    internal fun tearDown() {
        reset(mockPrefs)
    }
}
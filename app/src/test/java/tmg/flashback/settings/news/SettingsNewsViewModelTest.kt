package tmg.flashback.settings.news

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.enums.NewsSource
import tmg.flashback.repo.enums.ThemePref
import tmg.flashback.settings.SettingsViewModel
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.test

class SettingsNewsViewModelTest: BaseTest() {

    lateinit var sut: SettingsNewsViewModel

    private val mockPrefs: PrefsDB = mock()

    private val keyShowDescription: String = "keyShowDescription"
    private val keyJavascript: String = "keyJavascript"

    @BeforeEach
    internal fun setUp() {

        whenever(mockPrefs.newsShowDescription).thenReturn(false)
        whenever(mockPrefs.inAppEnableJavascript).thenReturn(false)
        whenever(mockPrefs.newsSourceExcludeList).thenReturn(emptySet())

        sut = SettingsNewsViewModel(mockPrefs)
    }

    @Test
    fun `SettingsNewsViewModel settings list is returned properly`() {

        val expected = listOf(
            AppPreferencesItem.Category(R.string.settings_news_appearance_title),
            AppPreferencesItem.SwitchPreference(
                keyShowDescription,
                R.string.settings_news_show_description_title,
                R.string.settings_news_show_description_description,
                false
            ),
            AppPreferencesItem.Category(R.string.settings_news_sources),
            AppPreferencesItem.SwitchPreference(
                prefKey = "autosport",
                title = R.string.settings_news_sources_autosport_title,
                description = R.string.settings_news_sources_autosport_description,
                isChecked = true
            ),
            AppPreferencesItem.SwitchPreference(
                prefKey = "pitpass",
                title = R.string.settings_news_sources_pitpass_title,
                description = R.string.settings_news_sources_pitpass_description,
                isChecked = true
            ),
            AppPreferencesItem.SwitchPreference(
                prefKey = "crashnet",
                title = R.string.settings_news_sources_crash_net_title,
                description = R.string.settings_news_sources_crash_net_description,
                isChecked = true
            ),
            AppPreferencesItem.SwitchPreference(
                prefKey = "motorsport",
                title = R.string.settings_news_sources_motorsport_title,
                description = R.string.settings_news_sources_motorsport_description,
                isChecked = true
            ),
            AppPreferencesItem.Category(R.string.settings_news_browser),
            AppPreferencesItem.SwitchPreference(
                keyJavascript,
                R.string.settings_news_browser_javascript_title,
                R.string.settings_news_browser_javascript_description,
                false
            )
        )

        assertEquals(expected, sut.outputs.settings.test().latestValue())
    }

    @ParameterizedTest
    @CsvSource(
        "autosport",
        "pitpass",
        "crashnet",
        "motorsport"
    )
    fun `SettingsNewsViewModel update news source disabled adds it to the exclusion list`(newsPrefKey: String) {

        val newsSourceItem = NewsSource.values().first { it.key == newsPrefKey }

        sut.updateNewsSourcePref(newsSourceItem, false)

        val expected = setOf(newsSourceItem)

        verify(mockPrefs).newsSourceExcludeList = expected

    }

    @Test
    fun `SettingsNewsViewModel update show description marks it enabled in prefs`() {

        sut.updatePref(keyShowDescription, true)

        verify(mockPrefs).newsShowDescription = true
    }

    @Test
    fun `SettingsNewsViewModel update enable javascript marks it enabled in prefs`() {

        sut.updatePref(keyJavascript, true)

        verify(mockPrefs).inAppEnableJavascript = true
    }

    @AfterEach
    internal fun tearDown() {
        reset(mockPrefs)
    }
}
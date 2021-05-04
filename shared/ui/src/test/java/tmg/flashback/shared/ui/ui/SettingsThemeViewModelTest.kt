package tmg.flashback.shared.ui.ui

import org.junit.jupiter.api.Test
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.shared.ui.R
import tmg.test.assertEventFired
import tmg.test.test

internal class SettingsThemeViewModelTest {

    private lateinit var sut: SettingsThemeViewModel

    private fun initSUT() {
        sut = SettingsThemeViewModel()
    }

    @Test
    fun `init loads correct settings options`() {
        initSUT()
        sut.outputs.settings.test {
            assertValue(listOf(
                AppPreferencesItem.Category(R.string.settings_theme_title),
                AppPreferencesItem.Preference("Theme", R.string.settings_theme_theme_title, R.string.settings_theme_theme_description),
                AppPreferencesItem.Preference("AnimationSpeed", R.string.settings_theme_animation_speed_title, R.string.settings_theme_animation_speed_description)
            ))
        }
    }

    //region Theme

    @Test
    fun `clicking theme in settings calls open theme event`() {
        initSUT()
        sut.inputs.preferenceClicked("Theme", null)
        sut.outputs.openTheme.test {
            assertEventFired()
        }
    }

    //endregion

    @Test
    fun `clicking animation speed in settings calls open animation speed event`() {
        initSUT()
        sut.inputs.preferenceClicked("AnimationSpeed", null)
        sut.outputs.openAnimationSpeed.test {
            assertEventFired()
        }
    }


}
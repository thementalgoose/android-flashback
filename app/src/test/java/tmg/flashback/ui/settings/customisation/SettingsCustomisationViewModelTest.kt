package tmg.flashback.ui.settings.customisation

import org.junit.jupiter.api.Test
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertEventFired
import tmg.flashback.testutils.test

internal class SettingsCustomisationViewModelTest: BaseTest() {

    private lateinit var sut: SettingsCustomisationViewModel

    private fun initSUT() {
        sut = SettingsCustomisationViewModel()
    }

    @Test
    fun `init loads correct settings options`() {
        initSUT()
        sut.outputs.settings.test {
            assertValue(listOf(
                AppPreferencesItem.Category(R.string.settings_customisation),
                AppPreferencesItem.Preference("Theme", R.string.settings_theme_theme_title, R.string.settings_theme_theme_description),
                AppPreferencesItem.Preference("AnimationSpeed", R.string.settings_animation_speed_animation_title, R.string.settings_animation_speed_animation_description)
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
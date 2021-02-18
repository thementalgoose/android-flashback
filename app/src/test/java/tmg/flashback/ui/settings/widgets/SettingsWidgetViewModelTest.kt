package tmg.flashback.ui.settings.widgets

import org.junit.jupiter.api.Test
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertEventFired
import tmg.flashback.testutils.test

internal class SettingsWidgetViewModelTest: BaseTest() {

    private lateinit var sut: SettingsWidgetViewModel

    private fun initSUT() {
        sut = SettingsWidgetViewModel()
    }

    @Test
    fun `init loads correct settings options`() {
        initSUT()
        sut.outputs.settings.test {
            assertValue(listOf(
                AppPreferencesItem.Category(R.string.settings_widgets),
                AppPreferencesItem.Preference("Widgets", R.string.settings_widgets_update_all_title, R.string.settings_widgets_update_all_description)
            ))
        }
    }

    @Test
    fun `clicking refresh widgets launches open event`() {
        initSUT()
        sut.inputs.preferenceClicked("Widgets", null)
        sut.outputs.refreshWidget.test {
            assertEventFired()
        }
    }
}
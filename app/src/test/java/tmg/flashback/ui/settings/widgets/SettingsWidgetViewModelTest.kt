package tmg.flashback.ui.settings.widgets

import androidx.preference.SwitchPreference
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.components.prefs.AppPreferencesItem
import tmg.components.prefs.AppPreferencesItemType
import tmg.flashback.R
import tmg.flashback.data.repositories.AppRepository
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertEventFired
import tmg.flashback.testutils.assertListMatchesItem
import tmg.flashback.testutils.test

internal class SettingsWidgetViewModelTest: BaseTest() {

    private val mockAppRepository: AppRepository = mockk(relaxed = true)

    private lateinit var sut: SettingsWidgetViewModel

    @BeforeEach
    internal fun setUp() {
        every { mockAppRepository.widgetOpenApp } returns false
    }

    private fun initSUT() {
        sut = SettingsWidgetViewModel(mockAppRepository)
    }

    @Test
    fun `init loads correct settings options`() {
        initSUT()
        sut.outputs.settings.test {
            assertValue(listOf(
                AppPreferencesItem.Category(R.string.settings_widgets),
                AppPreferencesItem.Preference("Widgets", R.string.settings_widgets_update_all_title, R.string.settings_widgets_update_all_description),
                AppPreferencesItem.SwitchPreference("OpenApp", R.string.settings_widgets_open_app_on_click_title, R.string.settings_widgets_open_app_on_click_description, false)
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

    @Test
    fun `clicking open widget to false disables widget open and refresh event fired`() {
        initSUT()
        sut.inputs.preferenceClicked("OpenApp", false)

        verify {
            mockAppRepository.widgetOpenApp = false
        }

        sut.outputs.settings.test {
            assertListMatchesItem {
                it is AppPreferencesItem.SwitchPreference && it.prefKey == "OpenApp" && !it.isChecked
            }
        }
        sut.outputs.refreshWidget.test {
            assertEventFired()
        }
    }
}
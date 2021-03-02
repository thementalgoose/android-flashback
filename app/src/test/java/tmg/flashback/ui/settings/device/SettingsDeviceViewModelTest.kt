package tmg.flashback.ui.settings.device

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R
import tmg.flashback.core.controllers.AnalyticsController
import tmg.flashback.core.controllers.CrashController
import tmg.flashback.core.controllers.DeviceController
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertEventFired
import tmg.flashback.testutils.test

internal class SettingsDeviceViewModelTest : BaseTest() {

    private var mockCrashController: CrashController = mockk(relaxed = true)
    private var mockAnalyticsController: AnalyticsController = mockk(relaxed = true)
    private var mockDeviceController: DeviceController = mockk(relaxed = true)

    private lateinit var sut: SettingsDeviceViewModel

    @BeforeEach
    internal fun setUp() {
        every { mockCrashController.enabled } returns false
        every { mockAnalyticsController.enabled } returns false
        every { mockDeviceController.shakeToReport } returns false
    }

    private fun initSUT() {
        sut = SettingsDeviceViewModel(mockCrashController, mockAnalyticsController, mockDeviceController)
    }

    @Test
    fun `init loads correct settings options`() {
        initSUT()
        sut.outputs.settings.test {
            assertValue(listOf(
                    AppPreferencesItem.Category(R.string.settings_device),
                    AppPreferencesItem.Preference("ReleaseNotes", R.string.settings_help_release_notes_title, R.string.settings_help_release_notes_description),
                    AppPreferencesItem.SwitchPreference("Crash", R.string.settings_help_crash_reporting_title, R.string.settings_help_crash_reporting_description, false),
                    AppPreferencesItem.SwitchPreference("Analytics", R.string.settings_help_analytics_title, R.string.settings_help_analytics_description, false),
                    AppPreferencesItem.SwitchPreference("ShakeToReport", R.string.settings_help_shake_to_report_title, R.string.settings_help_shake_to_report_description, false)
            ))
        }
    }

    @Test
    fun `clicking crash toggle marks crash enabled`() {
        initSUT()
        sut.inputs.preferenceClicked("Crash", true)
        verify {
            mockCrashController.enabled = true
        }
    }

    @Test
    fun `clicking analytics toggle marks analytics enabled`() {
        initSUT()
        sut.inputs.preferenceClicked("Analytics", true)
        verify {
            mockAnalyticsController.enabled = true
        }
    }

    @Test
    fun `clicking shake toggle marks device shake support enabled`() {
        initSUT()
        sut.inputs.preferenceClicked("ShakeToReport", true)
        verify {
            mockDeviceController.shakeToReport = true
        }
    }

    @Test
    fun `clicking release notes opens release notes`() {
        initSUT()
        sut.inputs.preferenceClicked("ReleaseNotes", null)
        sut.outputs.openReleaseNotes.test {
            assertEventFired()
        }
    }

}
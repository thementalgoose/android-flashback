package tmg.common.ui.settings.support

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.core.analytics.manager.AnalyticsManager
import tmg.crash_reporting.R
import tmg.crash_reporting.controllers.CrashController

internal class SettingsSupportViewModelTest {

    private val mockCrashController: CrashController = mockk(relaxed = true)
    private val mockAnalyticsManager: AnalyticsManager = mockk(relaxed = true)

    private lateinit var sut: SettingsSupportViewModel

    private fun initSUT() {
        sut = SettingsSupportViewModel(mockCrashController, mockAnalyticsManager)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockCrashController.enabled } returns false
        every { mockCrashController.shakeToReport } returns false
    }

    @Test
    fun `init loads correct settings options`() {
        initSUT()
        sut.outputs.settings.test {
            assertValue(listOf(
                AppPreferencesItem.Category(R.string.settings_crash_reporting_title),
                AppPreferencesItem.SwitchPreference("CrashReporting",
                    R.string.settings_crash_reporting_enabled_title,
                    R.string.settings_crash_reporting_enabled_description, false),
                AppPreferencesItem.SwitchPreference("ShakeToReport",
                    R.string.settings_crash_reporting_shake_to_report_title,
                    R.string.settings_crash_reporting_shake_to_report_description, false),
            ))
        }
    }

    @Test
    fun `clicking enabled toggle marks crash reporting enabled`() {
        initSUT()
        sut.inputs.preferenceClicked("CrashReporting", true)
        verify {
            mockCrashController.enabled = true
        }
    }

    @Test
    fun `clicking enabled toggle sends notification event`() {
        initSUT()
        sut.inputs.preferenceClicked("CrashReporting", true)
        sut.outputs.notifyPreferencesAppliedAfterRestart.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking shake to report toggle marks shake to report enabled`() {
        initSUT()
        sut.inputs.preferenceClicked("ShakeToReport", true)
        verify {
            mockCrashController.shakeToReport = true
        }
    }

    @Test
    fun `clicking shake to report toggle sends notification event`() {
        initSUT()
        sut.inputs.preferenceClicked("ShakeToReport", true)
        sut.outputs.notifyPreferencesAppliedAfterRestart.test {
            assertEventFired()
        }
    }

}
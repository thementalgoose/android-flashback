package tmg.common.ui.settings.support

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.common.R
import tmg.common.testutils.findSwitch
import tmg.core.analytics.manager.AnalyticsManager
import tmg.core.ui.settings.SettingsModel
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

        val expected = listOf(
                Pair(R.string.settings_help, null),
                Pair(R.string.settings_help_crash_reporting_title, R.string.settings_help_crash_reporting_description),
                Pair(R.string.settings_help_analytics_title, R.string.settings_help_analytics_description),
                Pair(R.string.settings_help_shake_to_report_title, R.string.settings_help_shake_to_report_description),
        )

        sut.models.forEachIndexed { index, settingsModel ->
            if (settingsModel is SettingsModel.Header) {
                assertEquals(expected[index].first, settingsModel.title)
            }
            if (settingsModel is SettingsModel.SwitchPref) {
                assertEquals(expected[index].first, settingsModel.title)
                assertEquals(expected[index].second, settingsModel.description)
            }
            if (settingsModel is SettingsModel.Pref) {
                assertEquals(expected[index].first, settingsModel.title)
                assertEquals(expected[index].second, settingsModel.description)
            }
        }
    }

    @Test
    fun `clicking toggle for crash reporting updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_help_crash_reporting_title), true)
        verify {
            mockCrashController.enabled
            mockCrashController.enabled = true
        }
    }

    @Test
    fun `clicking toggle for anonymous analytics updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_help_analytics_title), true)
        verify {
            mockAnalyticsManager.enabled
            mockAnalyticsManager.enabled = true
        }
    }

    @Test
    fun `clicking toggle for shake to report updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_help_shake_to_report_title), true)
        verify {
            mockCrashController.shakeToReport
            mockCrashController.shakeToReport = true
        }
    }

}
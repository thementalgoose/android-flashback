package tmg.common.ui.settings.support

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.common.R
import tmg.common.testutils.assertExpectedOrder
import tmg.common.testutils.findSwitch
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.crash_reporting.controllers.CrashController
import tmg.testutils.BaseTest

internal class SettingsSupportViewModelTest: BaseTest() {

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

        sut.models.assertExpectedOrder(expected)
    }

    @Test
    fun `clicking toggle for crash reporting updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_help_crash_reporting_title), true)
        verify {
            mockCrashController.enabled = true
        }
    }

    @Test
    fun `clicking toggle for anonymous analytics updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_help_analytics_title), true)
        verify {
            mockAnalyticsManager.enabled = true
        }
    }

    @Test
    fun `clicking toggle for shake to report updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_help_shake_to_report_title), true)
        verify {
            mockCrashController.shakeToReport = true
        }
    }

}
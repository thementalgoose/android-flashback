package tmg.flashback.common.ui.settings.support

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.common.R
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.crash_reporting.repository.CrashRepository
import tmg.flashback.common.testutils.assertExpectedOrder
import tmg.flashback.common.testutils.findSwitch
import tmg.testutils.BaseTest

internal class SettingsSupportViewModelTest: BaseTest() {

    private val mockCrashRepository: CrashRepository = mockk(relaxed = true)
    private val mockAnalyticsManager: AnalyticsManager = mockk(relaxed = true)

    private lateinit var sut: SettingsSupportViewModel

    private fun initSUT() {
        sut = SettingsSupportViewModel(mockCrashRepository, mockAnalyticsManager)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockCrashRepository.isEnabled } returns false
        every { mockCrashRepository.shakeToReport } returns false
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
            mockCrashRepository.isEnabled = true
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
            mockCrashRepository.shakeToReport = true
        }
    }

}
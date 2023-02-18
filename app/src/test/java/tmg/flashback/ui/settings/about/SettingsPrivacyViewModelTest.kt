package tmg.flashback.ui.settings.about

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.R
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.crash_reporting.repository.CrashRepository
import tmg.flashback.privacypolicy.PrivacyPolicy
import tmg.flashback.ui.managers.ToastManager
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import tmg.flashback.ui.settings.Settings
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve

internal class SettingsPrivacyViewModelTest: BaseTest() {

    private val mockCrashRepository: CrashRepository = mockk(relaxed = true)
    private val mockAnalyticsManager: AnalyticsManager = mockk(relaxed = true)
    private val mockToastManager: ToastManager = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)

    private lateinit var underTest: SettingsPrivacyViewModel

    private fun initUnderTest() {
        underTest = SettingsPrivacyViewModel(
            crashRepository = mockCrashRepository,
            analyticsManager = mockAnalyticsManager,
            toastManager = mockToastManager,
            navigator = mockNavigator
        )
    }

    @Test
    fun `crash reporting enabled is true when crash reporting is enabled`() {
        every { mockCrashRepository.isEnabled } returns true

        initUnderTest()
        underTest.outputs.crashReportingEnabled.test {
            assertValue(true)
        }
    }

    @Test
    fun `crash reporting enabled is false when crash reporting is disabled`() {
        every { mockCrashRepository.isEnabled } returns false

        initUnderTest()
        underTest.outputs.crashReportingEnabled.test {
            assertValue(false)
        }
    }

    @Test
    fun `analytics enabled is true when analytics is enabled`() {
        every { mockAnalyticsManager.enabled } returns true

        initUnderTest()
        underTest.outputs.analyticsEnabled.test {
            assertValue(true)
        }
    }

    @Test
    fun `analytics enabled is false when analytics is disabled`() {
        every { mockAnalyticsManager.enabled } returns false

        initUnderTest()
        underTest.outputs.analyticsEnabled.test {
            assertValue(false)
        }
    }

    @Test
    fun `click privacy policy opens privacy policy`() {
        initUnderTest()
        underTest.inputs.prefClicked(Settings.Other.privacyPolicy)

        verify {
            mockNavigator.navigate(Screen.Settings.PrivacyPolicy)
        }
    }

    @Test
    fun `click crash reporting updates pref and updates value`() {
        every { mockCrashRepository.isEnabled } returns false

        initUnderTest()
        val observer = underTest.outputs.crashReportingEnabled.testObserve()
        underTest.inputs.prefClicked(Settings.Other.crashReporting(true))

        verify {
            mockCrashRepository.isEnabled = true
            mockToastManager.displayToast(R.string.settings_restart_app_required)
        }
        observer.assertEmittedCount(2)
    }

    @Test
    fun `click analytics updates pref and updates value`() {
        every { mockAnalyticsManager.enabled } returns false

        initUnderTest()
        val observer = underTest.outputs.analyticsEnabled.testObserve()
        underTest.inputs.prefClicked(Settings.Other.analytics(true))

        verify {
            mockAnalyticsManager.enabled = true
            mockToastManager.displayToast(R.string.settings_restart_app_required)
        }
        observer.assertEmittedCount(2)
    }
}
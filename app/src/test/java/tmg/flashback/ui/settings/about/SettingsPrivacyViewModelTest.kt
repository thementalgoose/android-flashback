package tmg.flashback.ui.settings.about

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.R
import tmg.flashback.device.repository.PrivacyRepository
import tmg.flashback.privacypolicy.contract.PrivacyPolicy
import tmg.flashback.ui.managers.ToastManager
import tmg.flashback.ui.settings.Settings
import tmg.testutils.BaseTest

internal class SettingsPrivacyViewModelTest: BaseTest() {

    private val mockPrivacyRepository: PrivacyRepository = mockk(relaxed = true)
    private val mockToastManager: ToastManager = mockk(relaxed = true)
    private val mockNavigator: tmg.flashback.navigation.Navigator = mockk(relaxed = true)

    private lateinit var underTest: SettingsPrivacyViewModel

    private fun initUnderTest() {
        underTest = SettingsPrivacyViewModel(
            privacyRepository = mockPrivacyRepository,
            toastManager = mockToastManager,
            navigator = mockNavigator
        )
    }

    @Test
    fun `crash reporting enabled is true when crash reporting is enabled`() = runTest {
        every { mockPrivacyRepository.crashReporting } returns true

        initUnderTest()
        underTest.outputs.crashReportingEnabled.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `crash reporting enabled is false when crash reporting is disabled`() = runTest {
        every { mockPrivacyRepository.crashReporting } returns false

        initUnderTest()
        underTest.outputs.crashReportingEnabled.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `analytics enabled is true when analytics is enabled`() = runTest {
        every { mockPrivacyRepository.analytics } returns true

        initUnderTest()
        underTest.outputs.analyticsEnabled.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `analytics enabled is false when analytics is disabled`() = runTest {
        every { mockPrivacyRepository.analytics } returns false

        initUnderTest()
        underTest.outputs.analyticsEnabled.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `click privacy policy opens privacy policy`() = runTest {
        initUnderTest()
        underTest.inputs.prefClicked(Settings.Other.privacyPolicy)

        verify {
            mockNavigator.navigate(tmg.flashback.navigation.Screen.Settings.PrivacyPolicy)
        }
    }

    @Test
    fun `click crash reporting updates pref and updates value`() = runTest {
        every { mockPrivacyRepository.crashReporting } returns false

        initUnderTest()
        underTest.outputs.crashReportingEnabled.test { awaitItem() }
        underTest.inputs.prefClicked(Settings.Other.crashReporting(true))

        verify {
            mockPrivacyRepository.crashReporting = true
            mockToastManager.displayToast(R.string.settings_restart_app_required)
        }
        underTest.outputs.crashReportingEnabled.test { awaitItem() }
    }

    @Test
    fun `click analytics updates pref and updates value`() = runTest {
        every { mockPrivacyRepository.analytics } returns false

        initUnderTest()
        underTest.outputs.analyticsEnabled.test { awaitItem() }
        underTest.inputs.prefClicked(Settings.Other.analytics(true))

        verify {
            mockPrivacyRepository.analytics = true
            mockToastManager.displayToast(R.string.settings_restart_app_required)
        }
        underTest.outputs.analyticsEnabled.test { awaitItem() }
    }
}
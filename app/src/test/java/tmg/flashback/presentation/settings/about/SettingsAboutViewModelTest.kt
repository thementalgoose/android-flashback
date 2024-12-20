package tmg.flashback.presentation.settings.about

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.R
import tmg.flashback.strings.R.string
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.device.repository.PrivacyRepository
import tmg.flashback.device.usecases.OpenPlayStoreUseCase
import tmg.flashback.navigation.ApplicationNavigationComponent
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.ui.managers.ToastManager
import tmg.flashback.presentation.settings.Settings
import tmg.flashback.reactiongame.contract.Reaction
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.testutils.BaseTest

internal class SettingsAboutViewModelTest: BaseTest() {

    private val mockPrivacyRepository: PrivacyRepository = mockk(relaxed = true)
    private val mockApplicationNavigationComponent: ApplicationNavigationComponent = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)
    private val mockOpenPlaystoreUseCase: OpenPlayStoreUseCase = mockk(relaxed = true)
    private val mockToastManager: ToastManager = mockk(relaxed = true)

    private lateinit var underTest: SettingsAboutViewModel

    private fun initUnderTest() {
        underTest = SettingsAboutViewModel(
            privacyRepository = mockPrivacyRepository,
            applicationNavigationComponent = mockApplicationNavigationComponent,
            navigator = mockNavigator,
            openPlayStoreUseCase = mockOpenPlaystoreUseCase,
            toastManager = mockToastManager
        )
    }

    @Test
    fun `shake to report is enabled when pref is true`() = runTest(testDispatcher) {
        every { mockPrivacyRepository.shakeToReport } returns true

        initUnderTest()
        underTest.outputs.shakeToReportEnabled.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `shake to report is disabled when pref is disabled`() = runTest(testDispatcher) {
        every { mockPrivacyRepository.shakeToReport } returns false

        initUnderTest()
        underTest.outputs.shakeToReportEnabled.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `click about this app launches about this app screen`() {
        initUnderTest()
        underTest.inputs.prefClicked(Settings.Other.aboutThisApp)

        verify {
            mockApplicationNavigationComponent.aboutApp()
        }
    }

    @Test
    fun `click review launches review screen`() {
        initUnderTest()
        underTest.inputs.prefClicked(Settings.Other.review)

        verify {
            mockOpenPlaystoreUseCase.openPlaystore()
        }
    }

    @Test
    fun `click build number does nothing`() {
        initUnderTest()
        underTest.inputs.prefClicked(Settings.Other.buildVersion)

        verify(exactly = 0) {
            mockNavigator.navigate(any())
        }
    }

    @Test
    fun `click reset first time opens sync activity`() {
        initUnderTest()
        underTest.inputs.prefClicked(Settings.Other.resetFirstTimeSync)

        verify {
            mockApplicationNavigationComponent.syncActivity()
        }
    }

    @Test
    fun `click build number 5 times does nothing`() {
        initUnderTest()
        underTest.inputs.prefClicked(Settings.Other.buildVersion)
        underTest.inputs.prefClicked(Settings.Other.buildVersion)
        underTest.inputs.prefClicked(Settings.Other.buildVersion)
        underTest.inputs.prefClicked(Settings.Other.buildVersion)

        verify(exactly = 0) {
            mockNavigator.navigate(any())
        }

        underTest.inputs.prefClicked(Settings.Other.buildVersion)

        verify(exactly = 1) {
            mockNavigator.navigate(Screen.Reaction)
        }
    }

    @Test
    fun `click shake to report updates pref and updates value`() = runTest(testDispatcher) {
        every { mockPrivacyRepository.shakeToReport } returns false
        initUnderTest()
        underTest.outputs.shakeToReportEnabled.test { awaitItem() }
        underTest.inputs.prefClicked(Settings.Other.shakeToReport(true))

        verify {
            mockPrivacyRepository.shakeToReport = true
            mockToastManager.displayToast(string.settings_restart_app_required)
        }
        underTest.outputs.shakeToReportEnabled.test { awaitItem() }
    }

    companion object {
        private const val APPLICATION_ID: String = "applicationId"
        private const val REVIEW_URL: String = "https://play.google.com/store/apps/details?id=$APPLICATION_ID"
    }

}
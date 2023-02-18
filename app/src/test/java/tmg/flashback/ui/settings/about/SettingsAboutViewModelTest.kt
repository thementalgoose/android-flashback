package tmg.flashback.ui.settings.about

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.R
import tmg.flashback.crash_reporting.repository.CrashRepository
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.releasenotes.ReleaseNotes
import tmg.flashback.ui.managers.ToastManager
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import tmg.flashback.ui.settings.Settings
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve

internal class SettingsAboutViewModelTest: BaseTest() {

    private val mockCrashRepository: CrashRepository = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)
    private val mockApplicationNavigationComponent: ApplicationNavigationComponent = mockk(relaxed = true)
    private val mockOpenWebpageUseCase: OpenWebpageUseCase = mockk(relaxed = true)
    private val mockToastManager: ToastManager = mockk(relaxed = true)
    private val mockBuildConfigManager: BuildConfigManager = mockk(relaxed = true)

    private lateinit var underTest: SettingsAboutViewModel

    private fun initUnderTest() {
        underTest = SettingsAboutViewModel(
            crashRepository = mockCrashRepository,
            navigator = mockNavigator,
            applicationNavigationComponent = mockApplicationNavigationComponent,
            openWebpageUseCase = mockOpenWebpageUseCase,
            toastManager = mockToastManager,
            buildConfigManager = mockBuildConfigManager,
        )
    }

    @Test
    fun `shake to report is enabled when pref is true`() {
        every { mockCrashRepository.shakeToReport } returns true

        initUnderTest()
        underTest.outputs.shakeToReportEnabled.test {
            assertValue(true)
        }
    }

    @Test
    fun `shake to report is disabled when pref is disabled`() {
        every { mockCrashRepository.shakeToReport } returns false

        initUnderTest()
        underTest.outputs.shakeToReportEnabled.test {
            assertValue(false)
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
        every { mockBuildConfigManager.applicationId } returns APPLICATION_ID
        initUnderTest()
        underTest.inputs.prefClicked(Settings.Other.review)

        verify {
            mockOpenWebpageUseCase.open(url = REVIEW_URL, title = "")
        }
    }

    @Test
    fun `click release notes launches release notes`() {
        initUnderTest()
        underTest.inputs.prefClicked(Settings.Other.releaseNotes)

        verify {
            mockNavigator.navigate(Screen.ReleaseNotes)
        }
    }

    @Test
    fun `click shake to report updates pref and updates value`() {
        every { mockCrashRepository.shakeToReport } returns false
        initUnderTest()
        val observer = underTest.outputs.shakeToReportEnabled.testObserve()
        underTest.inputs.prefClicked(Settings.Other.shakeToReport(true))

        verify {
            mockCrashRepository.shakeToReport = true
            mockToastManager.displayToast(R.string.settings_restart_app_required)
        }
        observer.assertEmittedCount(2)
    }

    companion object {
        private const val APPLICATION_ID: String = "applicationId"
        private const val REVIEW_URL: String = "https://play.google.com/store/apps/details?id=$APPLICATION_ID"
    }

}
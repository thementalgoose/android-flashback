package tmg.flashback.ui.settings.about

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.crash_reporting.repository.CrashRepository
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.releasenotes.ReleaseNotesNavigationComponent
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import tmg.flashback.ui.settings.Settings
import tmg.flashback.web.WebNavigationComponent
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve

internal class SettingsAboutViewModelTest: BaseTest() {

    private val mockCrashRepository: CrashRepository = mockk(relaxed = true)
    private val mockReleaseNotesNavigationComponent: ReleaseNotesNavigationComponent = mockk(relaxed = true)
    private val mockApplicationNavigationComponent: ApplicationNavigationComponent = mockk(relaxed = true)
    private val mockWebNavigationComponent: WebNavigationComponent = mockk(relaxed = true)
    private val mockBuildConfigManager: BuildConfigManager = mockk(relaxed = true)

    private lateinit var underTest: SettingsAboutViewModel

    private fun initUnderTest() {
        underTest = SettingsAboutViewModel(
            crashRepository = mockCrashRepository,
            releaseNotesNavigationComponent = mockReleaseNotesNavigationComponent,
            applicationNavigationComponent = mockApplicationNavigationComponent,
            webNavigationComponent = mockWebNavigationComponent,
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
            mockWebNavigationComponent.web(REVIEW_URL)
        }
    }

    @Test
    fun `click release notes launches release notes`() {
        initUnderTest()
        underTest.inputs.prefClicked(Settings.Other.releaseNotes)

        verify {
            mockReleaseNotesNavigationComponent.releaseNotes()
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
        }
        observer.assertEmittedCount(2)
    }

    companion object {
        private const val APPLICATION_ID: String = "applicationId"
        private const val REVIEW_URL: String = "http://play.google.com/store/apps/details?id=$APPLICATION_ID"
    }

}
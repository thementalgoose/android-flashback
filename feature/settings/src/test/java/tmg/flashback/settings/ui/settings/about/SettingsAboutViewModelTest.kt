package tmg.flashback.settings.ui.settings.about

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.releasenotes.ReleaseNotesNavigationComponent
import tmg.flashback.settings.R
import tmg.flashback.settings.SettingsNavigationComponent
import tmg.flashback.settings.testutils.assertExpectedOrder
import tmg.flashback.settings.testutils.findPref
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class SettingsAboutViewModelTest: BaseTest() {

    private val mockApplicationNavigationComponent: ApplicationNavigationComponent = mockk(relaxed = true)
    private val mockReleaseNotesNavigationComponent: ReleaseNotesNavigationComponent = mockk(relaxed = true)
    private val mockSettingsNavigationComponent: SettingsNavigationComponent = mockk(relaxed = true)
    private val mockBuildConfigManager: BuildConfigManager = mockk(relaxed = true)

    private lateinit var sut: SettingsAboutViewModel

    private fun initSUT() {
        sut = SettingsAboutViewModel(
            mockApplicationNavigationComponent,
            mockReleaseNotesNavigationComponent,
            mockSettingsNavigationComponent,
            mockBuildConfigManager
        )
    }

    @Test
    fun `initial model list is expected`() {
        initSUT()
        val expected = listOf(
                Pair(R.string.settings_about, null),
                Pair(R.string.settings_about_about_title, R.string.settings_about_about_description),
                Pair(R.string.settings_about_review_title, R.string.settings_about_review_description),
                Pair(R.string.settings_about_release_notes_title, R.string.settings_about_release_notes_description),
                Pair(R.string.settings_about_privacy_policy_title, R.string.settings_about_privacy_policy_description)
        )

        sut.models.assertExpectedOrder(expected)
    }

    @Test
    fun `clicking pref model for about shows event`() {
        initSUT()
        sut.models.findPref(R.string.settings_about_about_title).onClick?.invoke()
        verify {
            mockApplicationNavigationComponent.aboutApp()
        }
    }

    @Test
    fun `clicking pref model for review shows event`() {

        every { mockBuildConfigManager.applicationId } returns "applicationId"
        val expected = "http://play.google.com/store/apps/details?id=applicationId"

        initSUT()
        sut.models.findPref(R.string.settings_about_review_title).onClick?.invoke()
        verify {
            mockApplicationNavigationComponent.openUrl(expected)
        }
    }

    @Test
    fun `clicking pref model for release notes shows event`() {
        initSUT()
        sut.models.findPref(R.string.settings_about_release_notes_title).onClick?.invoke()
        verify {
            mockReleaseNotesNavigationComponent.releaseNotes()
        }
    }

    @Test
    fun `clicking pref model for privacy policy shows event`() {
        initSUT()
        sut.models.findPref(R.string.settings_about_privacy_policy_title).onClick?.invoke()
        verify {
            mockSettingsNavigationComponent.privacyPolicy()
        }
    }
}
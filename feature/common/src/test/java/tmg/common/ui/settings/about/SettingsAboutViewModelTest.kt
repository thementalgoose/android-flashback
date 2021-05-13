package tmg.common.ui.settings.about

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.common.R
import tmg.core.ui.settings.SettingsModel
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class SettingsAboutViewModelTest {

    private lateinit var sut: SettingsAboutViewModel

    private fun initSUT() {
        sut = SettingsAboutViewModel()
    }

    @Test
    fun `initial model list is expected`() {
        initSUT()
        (sut.models[0] as SettingsModel.Header).apply {
            assertEquals(R.string.settings_about, this.title)
        }
        (sut.models[1] as SettingsModel.Pref).apply {
            assertEquals(R.string.settings_about_about_title, this.title)
            assertEquals(R.string.settings_about_about_description, this.description)
        }
        (sut.models[2] as SettingsModel.Pref).apply {
            assertEquals(R.string.settings_about_review_title, this.title)
            assertEquals(R.string.settings_about_review_description, this.description)
        }
        (sut.models[3] as SettingsModel.Pref).apply {
            assertEquals(R.string.settings_about_release_notes_title, this.title)
            assertEquals(R.string.settings_about_release_notes_description, this.description)
        }
        (sut.models[4] as SettingsModel.Pref).apply {
            assertEquals(R.string.settings_about_privacy_policy_title, this.title)
            assertEquals(R.string.settings_about_privacy_policy_description, this.description)
        }
    }

    @Test
    fun `clicking pref model for about shows event`() {
        initSUT()
        (sut.models[1] as SettingsModel.Pref).apply {
            onClick?.invoke()
        }
        sut.outputs.openAboutThisApp.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking pref model for review shows event`() {
        initSUT()
        (sut.models[2] as SettingsModel.Pref).apply {
            onClick?.invoke()
        }
        sut.outputs.openReview.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking pref model for release notes shows event`() {
        initSUT()
        (sut.models[3] as SettingsModel.Pref).apply {
            onClick?.invoke()
        }
        sut.outputs.openReleaseNotes.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking pref model for privacy policy shows event`() {
        initSUT()
        (sut.models[4] as SettingsModel.Pref).apply {
            onClick?.invoke()
        }
        sut.outputs.openPrivacyPolicy.test {
            assertEventFired()
        }
    }
}
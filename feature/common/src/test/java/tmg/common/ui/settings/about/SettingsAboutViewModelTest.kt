package tmg.common.ui.settings.about

import org.junit.jupiter.api.Test
import tmg.common.R
import tmg.common.testutils.assertExpectedOrder
import tmg.common.testutils.findPref
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class SettingsAboutViewModelTest: BaseTest() {

    private lateinit var sut: SettingsAboutViewModel

    private fun initSUT() {
        sut = SettingsAboutViewModel()
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
        sut.outputs.openAboutThisApp.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking pref model for review shows event`() {
        initSUT()
        sut.models.findPref(R.string.settings_about_review_title).onClick?.invoke()
        sut.outputs.openReview.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking pref model for release notes shows event`() {
        initSUT()
        sut.models.findPref(R.string.settings_about_release_notes_title).onClick?.invoke()
        sut.outputs.openReleaseNotes.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking pref model for privacy policy shows event`() {
        initSUT()
        sut.models.findPref(R.string.settings_about_privacy_policy_title).onClick?.invoke()
        sut.outputs.openPrivacyPolicy.test {
            assertEventFired()
        }
    }
}
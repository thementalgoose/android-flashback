package tmg.flashback.ui.settings.about

import org.junit.jupiter.api.Test
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertEventFired
import tmg.flashback.testutils.test

internal class SettingsAboutViewModelTest: BaseTest() {

    private lateinit var sut: SettingsAboutViewModel

    private fun initSUT() {
        sut = SettingsAboutViewModel()
    }

    @Test
    fun `init loads correct settings options`() {
        initSUT()
        sut.outputs.settings.test {
            assertValue(listOf(
                AppPreferencesItem.Category(R.string.settings_help),
                AppPreferencesItem.Preference("AboutThisApp", R.string.settings_help_about_title, R.string.settings_help_about_description),
                AppPreferencesItem.Preference("Review", R.string.settings_help_review_title, R.string.settings_help_review_description),
                AppPreferencesItem.Preference("PrivacyPolicy", R.string.settings_help_privacy_policy_title, R.string.settings_help_privacy_policy_description)
            ))
        }
    }

    @Test
    fun `clicking about this app launches open event`() {
        initSUT()
        sut.inputs.preferenceClicked("AboutThisApp", null)
        sut.outputs.openAboutThisApp.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking review launches open event`() {
        initSUT()
        sut.inputs.preferenceClicked("Review", null)
        sut.outputs.openReview.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking privacy policy launches open event`() {
        initSUT()
        sut.inputs.preferenceClicked("PrivacyPolicy", null)
        sut.outputs.openPrivacyPolicy.test {
            assertEventFired()
        }
    }
}
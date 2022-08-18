package tmg.flashback.settings.ui.settings.about

import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.releasenotes.ReleaseNotesNavigationComponent
import tmg.flashback.settings.R
import tmg.flashback.settings.SettingsNavigationComponent
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import tmg.flashback.ui.settings.SettingsModel
import tmg.flashback.ui.settings.SettingsViewModel
import tmg.flashback.web.WebNavigationComponent
import javax.inject.Inject

//region Inputs

interface SettingsAboutViewModelInputs {

}

//endregion

//region Outputs

interface SettingsAboutViewModelOutputs {
}

//endregion

@HiltViewModel
class SettingsAboutViewModel @Inject constructor(
    private val applicationNavigationComponent: ApplicationNavigationComponent,
    private val webNavigationComponent: WebNavigationComponent,
    private val releaseNotesNavigationComponent: ReleaseNotesNavigationComponent,
    private val settingsNavigationComponent: SettingsNavigationComponent,
    private val buildConfigManager: BuildConfigManager
): SettingsViewModel(), SettingsAboutViewModelInputs, SettingsAboutViewModelOutputs {

    private val reviewUrl: String get() = "http://play.google.com/store/apps/details?id=${buildConfigManager.applicationId}"

    override val models: List<SettingsModel> = listOf(
        SettingsModel.Header(R.string.settings_about),
        SettingsModel.Pref(
            title = R.string.settings_about_about_title,
            description = R.string.settings_about_about_description,
            onClick = {
                applicationNavigationComponent.aboutApp()
            }
        ),
        SettingsModel.Pref(
            title = R.string.settings_about_review_title,
            description = R.string.settings_about_review_description,
            onClick = {
                webNavigationComponent.web(reviewUrl)
            }
        ),
        SettingsModel.Pref(
            title = R.string.settings_about_release_notes_title,
            description = R.string.settings_about_release_notes_description,
            onClick = {
                releaseNotesNavigationComponent.releaseNotes()
            }
        ),
        SettingsModel.Pref(
            title = R.string.settings_about_privacy_policy_title,
            description = R.string.settings_about_privacy_policy_description,
            onClick = {
                settingsNavigationComponent.privacyPolicy()
            }
        )
    )

    var inputs: SettingsAboutViewModelInputs = this
    var outputs: SettingsAboutViewModelOutputs = this
}

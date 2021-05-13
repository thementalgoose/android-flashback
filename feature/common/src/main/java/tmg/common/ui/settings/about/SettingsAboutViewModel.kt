package tmg.common.ui.settings.about

import android.widget.Toast
import tmg.common.R
import tmg.common.ui.settings.SettingsModel
import tmg.common.ui.settings.SettingsViewModel

//region Inputs

interface SettingsAboutViewModelInputs {

}

//endregion

//region Outputs

interface SettingsAboutViewModelOutputs {

}

//endregion


class SettingsAboutViewModel: SettingsViewModel(), SettingsAboutViewModelInputs, SettingsAboutViewModelOutputs {

    override val models: List<SettingsModel> = listOf(
        SettingsModel.Header(R.string.settings_about),
        SettingsModel.Pref(
            title = R.string.settings_about_about_title,
            description = R.string.settings_about_about_description,
            onClick = {
                TODO("About clicked")
            }
        ),
        SettingsModel.Pref(
            title = R.string.settings_about_review_title,
            description = R.string.settings_about_review_description,
            onClick = {
                TODO("Review clicked")
            }
        ),
        SettingsModel.Pref(
            title = R.string.settings_about_release_notes_title,
            description = R.string.settings_about_release_notes_description,
            onClick = {
                TODO("Release notes clicked")
            }
        ),
        SettingsModel.Pref(
            title = R.string.settings_about_privacy_policy_title,
            description = R.string.settings_about_privacy_policy_description,
            onClick = {
                TODO("Privacy Policy clicked")
            }
        )
    )

    var inputs: SettingsAboutViewModelInputs = this
    var outputs: SettingsAboutViewModelOutputs = this
}

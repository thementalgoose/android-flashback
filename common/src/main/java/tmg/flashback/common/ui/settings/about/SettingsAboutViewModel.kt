package tmg.flashback.common.ui.settings.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.flashback.common.R
import tmg.flashback.ui.settings.SettingsModel
import tmg.flashback.ui.settings.SettingsViewModel
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsAboutViewModelInputs {

}

//endregion

//region Outputs

interface SettingsAboutViewModelOutputs {
    val openAboutThisApp: LiveData<Event>
    val openReview: LiveData<Event>
    val openReleaseNotes: LiveData<Event>
    val openPrivacyPolicy: LiveData<Event>
}

//endregion


class SettingsAboutViewModel: SettingsViewModel(), SettingsAboutViewModelInputs, SettingsAboutViewModelOutputs {

    override val models: List<SettingsModel> = listOf(
        SettingsModel.Header(R.string.settings_about),
        SettingsModel.Pref(
            title = R.string.settings_about_about_title,
            description = R.string.settings_about_about_description,
            onClick = {
                openAboutThisApp.value = Event()
            }
        ),
        SettingsModel.Pref(
            title = R.string.settings_about_review_title,
            description = R.string.settings_about_review_description,
            onClick = {
                openReview.value = Event()
            }
        ),
        SettingsModel.Pref(
            title = R.string.settings_about_release_notes_title,
            description = R.string.settings_about_release_notes_description,
            onClick = {
                openReleaseNotes.value = Event()
            }
        ),
        SettingsModel.Pref(
            title = R.string.settings_about_privacy_policy_title,
            description = R.string.settings_about_privacy_policy_description,
            onClick = {
                openPrivacyPolicy.value = Event()
            }
        )
    )

    var inputs: SettingsAboutViewModelInputs = this
    var outputs: SettingsAboutViewModelOutputs = this

    override val openAboutThisApp: MutableLiveData<Event> = MutableLiveData()
    override val openReview: MutableLiveData<Event> = MutableLiveData()
    override val openReleaseNotes: MutableLiveData<Event> = MutableLiveData()
    override val openPrivacyPolicy: MutableLiveData<Event> = MutableLiveData()
}

package tmg.flashback.ui.settings.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R
import tmg.flashback.core.ui.BaseViewModel
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsAboutViewModelInputs {
    fun preferenceClicked(pref: String?, value: Boolean?)
}

//endregion

//region Outputs

interface SettingsAboutViewModelOutputs {
    val settings: LiveData<List<AppPreferencesItem>>

    val openAboutThisApp: LiveData<Event>
    val openReview: LiveData<Event>
    val openPrivacyPolicy: LiveData<Event>
}

//endregion

class SettingsAboutViewModel: BaseViewModel(), SettingsAboutViewModelInputs,
    SettingsAboutViewModelOutputs {

    private val keyAboutThisApp: String = "AboutThisApp"
    private val keyReview: String = "Review"
    private val keyPrivacyPolicy: String = "PrivacyPolicy"

    var inputs: SettingsAboutViewModelInputs = this
    var outputs: SettingsAboutViewModelOutputs = this

    override val settings: MutableLiveData<List<AppPreferencesItem>> = MutableLiveData()

    override val openAboutThisApp: MutableLiveData<Event> = MutableLiveData()
    override val openReview: MutableLiveData<Event> = MutableLiveData()
    override val openPrivacyPolicy: MutableLiveData<Event> = MutableLiveData()

    init {
        settings.value = mutableListOf<AppPreferencesItem>().apply {
            add(AppPreferencesItem.Category(R.string.settings_help))
            add(AppPreferencesItem.Preference(
                prefKey = keyAboutThisApp,
                title = R.string.settings_help_about_title,
                description = R.string.settings_help_about_description
            ))
            add(AppPreferencesItem.Preference(
                prefKey = keyReview,
                title = R.string.settings_help_review_title,
                description = R.string.settings_help_review_description
            ))
            add(AppPreferencesItem.Preference(
                prefKey = keyPrivacyPolicy,
                title = R.string.settings_help_privacy_policy_title,
                description = R.string.settings_help_privacy_policy_description
            ))
        }
    }

    //region Inputs

    override fun preferenceClicked(pref: String?, value: Boolean?) {
        when (pref) {
            keyAboutThisApp -> openAboutThisApp.value = Event()
            keyReview -> openReview.value = Event()
            keyPrivacyPolicy -> openPrivacyPolicy.value = Event()
        }
    }

    //endregion
}

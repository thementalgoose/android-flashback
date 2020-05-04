package tmg.flashback.settings

import android.content.Context
import androidx.lifecycle.MutableLiveData
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R
import tmg.flashback.base.BaseViewModel
import tmg.flashback.repo.db.PrefsDB
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsViewModelInputs {
    fun preferenceClicked(pref: SettingsOptions?, value: Boolean?)
}

//endregion

//region Outputs

interface SettingsViewModelOutputs {
    val settings: MutableLiveData<List<AppPreferencesItem>>

    val openAbout: MutableLiveData<Event>
    val openRelease: MutableLiveData<Event>
    val openSuggestions: MutableLiveData<Event>
}

//endregion

class SettingsViewModel(
    private val prefDB: PrefsDB,
    applicationContext: Context
): BaseViewModel(), SettingsViewModelInputs, SettingsViewModelOutputs {

    var inputs: SettingsViewModelInputs = this
    var outputs: SettingsViewModelOutputs = this

    override val settings: MutableLiveData<List<AppPreferencesItem>> = MutableLiveData()

    override val openAbout: MutableLiveData<Event> = MutableLiveData()
    override val openRelease: MutableLiveData<Event> = MutableLiveData()
    override val openSuggestions: MutableLiveData<Event> = MutableLiveData()

    init {
        settings.value = listOf(
            AppPreferencesItem.Category(applicationContext.getString(R.string.settings_customisation)),
            SettingsOptions.QUALIFYING_DELTAS.toSwitch(applicationContext, prefDB.showQualifyingDelta),
            AppPreferencesItem.Category(applicationContext.getString(R.string.settings_help)),
            SettingsOptions.ABOUT.toPref(applicationContext),
            SettingsOptions.RELEASE.toPref(applicationContext),
            AppPreferencesItem.Category(applicationContext.getString(R.string.settings_feedback)),
            SettingsOptions.CRASH.toSwitch(applicationContext, prefDB.crashReporting),
            SettingsOptions.SUGGESTION.toPref(applicationContext),
            SettingsOptions.SHAKE.toSwitch(applicationContext, prefDB.shakeToReport)
        )
    }

    //region Inputs

    override fun preferenceClicked(pref: SettingsOptions?, value: Boolean?) {
        when (pref) {
            SettingsOptions.QUALIFYING_DELTAS -> prefDB.showQualifyingDelta = value ?: false
            SettingsOptions.ABOUT -> openAbout.value = Event()
            SettingsOptions.RELEASE -> openRelease.value = Event()
            SettingsOptions.CRASH -> prefDB.crashReporting = value ?: true
            SettingsOptions.SUGGESTION -> openSuggestions.value = Event()
            SettingsOptions.SHAKE -> prefDB.shakeToReport = value ?: true
        }
    }

    //endregion

}
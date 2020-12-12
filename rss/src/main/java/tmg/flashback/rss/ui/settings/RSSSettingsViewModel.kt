package tmg.flashback.rss.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.rss.R
import tmg.flashback.rss.base.RSSBaseViewModel
import tmg.flashback.rss.prefs.RSSPrefsRepository
import tmg.utilities.lifecycle.Event

//region Inputs

interface RSSSettingsViewModelInputs {
    fun updatePref(pref: String, toNewState: Boolean)
    fun clickPref(pref: String)
}

//endregion

//region Outputs

interface RSSSettingsViewModelOutputs {
    val goToConfigure: LiveData<Event>
    val settings: MutableLiveData<List<AppPreferencesItem>>
}

//endregion

class RSSSettingsViewModel(
        private val prefRepository: RSSPrefsRepository
): RSSBaseViewModel(),
    RSSSettingsViewModelInputs,
    RSSSettingsViewModelOutputs {

    private val keyShowDescription: String = "keyShowDescription"
    private val keyConfigureSources: String = "keyConfigureSources"
    private val keyJavascript: String = "keyJavascript"
    private val keyOpenExternal: String = "keyOpenInExternalBrowser"

    var inputs: RSSSettingsViewModelInputs = this
    var outputs: RSSSettingsViewModelOutputs = this

    override val settings: MutableLiveData<List<AppPreferencesItem>> = MutableLiveData()

    override val goToConfigure: MutableLiveData<Event> = MutableLiveData()

    init {
        settings.value = populateList()
    }

    //region Inputs

    override fun clickPref(pref: String) {
        when (pref) {
            keyConfigureSources -> goToConfigure.postValue(Event())
        }
    }

    override fun updatePref(pref: String, toNewState: Boolean) {
        when (pref) {
            keyShowDescription -> prefRepository.rssShowDescription = toNewState
            keyJavascript -> prefRepository.inAppEnableJavascript = toNewState
            keyOpenExternal -> prefRepository.newsOpenInExternalBrowser = toNewState
        }
    }

    //endregion

    private fun populateList(): List<AppPreferencesItem> {

        val list: MutableList<AppPreferencesItem> = mutableListOf()
        list.add(AppPreferencesItem.Category(R.string.settings_rss_configure))
        list.add(AppPreferencesItem.Preference(
            prefKey = keyConfigureSources,
            title = R.string.settings_rss_configure_sources_title,
            description = R.string.settings_rss_configure_sources_description
        ))
        list.add(AppPreferencesItem.Category(R.string.settings_rss_appearance_title))
        list.add(AppPreferencesItem.SwitchPreference(
            keyShowDescription,
            R.string.settings_rss_show_description_title,
            R.string.settings_rss_show_description_description,
            prefRepository.rssShowDescription
        ))
        list.add(AppPreferencesItem.Category(R.string.settings_rss_browser))
        list.add(AppPreferencesItem.SwitchPreference(
            keyOpenExternal,
            R.string.settings_rss_browser_external_title,
            R.string.settings_rss_browser_external_description,
            prefRepository.newsOpenInExternalBrowser
        ))
        list.add(AppPreferencesItem.SwitchPreference(
            keyJavascript,
            R.string.settings_rss_browser_javascript_title,
            R.string.settings_rss_browser_javascript_description,
            prefRepository.inAppEnableJavascript
        ))
        return list
    }
}
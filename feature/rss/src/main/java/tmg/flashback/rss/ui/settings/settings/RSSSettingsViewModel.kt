package tmg.flashback.rss.ui.settings.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.core.ui.settings.SettingsModel
import tmg.core.ui.settings.SettingsViewModel
import tmg.flashback.rss.R
import tmg.flashback.rss.repo.RSSRepository
import tmg.utilities.lifecycle.Event

//region Inputs

interface RSSSettingsViewModelInputs {
}

//endregion

//region Outputs

interface RSSSettingsViewModelOutputs {
    val goToConfigure: LiveData<Event>
}

//endregion

class RSSSettingsViewModel(
        private val rssRepository: RSSRepository
): SettingsViewModel(), RSSSettingsViewModelInputs, RSSSettingsViewModelOutputs {

    override val models: List<SettingsModel> = listOf(
        SettingsModel.Header(R.string.settings_rss_configure),
        SettingsModel.Pref(
            title = R.string.settings_rss_configure_sources_title,
            description = R.string.settings_rss_configure_sources_description,
            onClick = { goToConfigure.value = Event() }
        ),
        SettingsModel.Header(R.string.settings_rss_appearance_title),
        SettingsModel.SwitchPref(
            title = R.string.settings_rss_show_description_title,
            description = R.string.settings_rss_show_description_description,
            getState = { rssRepository.rssShowDescription },
            saveState = { rssRepository.rssShowDescription = it }
        ),
        SettingsModel.Header(R.string.settings_rss_browser),
        SettingsModel.SwitchPref(
            title = R.string.settings_rss_browser_external_title,
            description = R.string.settings_rss_browser_external_description,
            getState = { rssRepository.newsOpenInExternalBrowser },
            saveState = { rssRepository.newsOpenInExternalBrowser = it }
        ),
        SettingsModel.SwitchPref(
            title = R.string.settings_rss_browser_javascript_title,
            description = R.string.settings_rss_browser_javascript_description,
            getState = { rssRepository.inAppEnableJavascript },
            saveState = { rssRepository.inAppEnableJavascript = it }
        ),

    )

    var inputs: RSSSettingsViewModelInputs = this
    var outputs: RSSSettingsViewModelOutputs = this

    override val goToConfigure: MutableLiveData<Event> = MutableLiveData()
}
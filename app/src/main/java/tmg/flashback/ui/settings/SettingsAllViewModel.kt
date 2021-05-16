package tmg.flashback.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.core.ui.settings.SettingsModel
import tmg.core.ui.settings.SettingsViewModel
import tmg.flashback.R
import tmg.flashback.rss.controllers.RSSController
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsAllViewModelInputs {

}

//endregion

//region Outputs

interface SettingsAllViewModelOutputs {
    val openAppearance: LiveData<Event>
    val openStatistics: LiveData<Event>
    val openRss: LiveData<Event>
    val openNotifications: LiveData<Event>
    val openSupport: LiveData<Event>
    val openAbout: LiveData<Event>

}

//endregion


class SettingsAllViewModel(
        private val rssController: RSSController
): SettingsViewModel(), SettingsAllViewModelInputs, SettingsAllViewModelOutputs {

    override val models: List<SettingsModel> = mutableListOf<SettingsModel>().apply {
        add(SettingsModel.Header(R.string.settings_title))
        add(SettingsModel.Pref(
                title = R.string.settings_all_appearance,
                description = R.string.settings_all_appearance_subtitle,
                onClick = {
                    openAppearance.value = Event()
                }
        ))
        add(SettingsModel.Pref(
                title = R.string.settings_all_statistics,
                description = R.string.settings_all_statistics_subtitle,
                onClick = {
                    openStatistics.value = Event()
                }
        ))
        if (rssController.enabled) {
            add(SettingsModel.Pref(
                    title = R.string.settings_all_rss,
                    description = R.string.settings_all_rss_subtitle,
                    onClick = {
                        openRss.value = Event()
                    }
            ))
        }
        add(SettingsModel.Pref(
                title = R.string.settings_all_notifications,
                description = R.string.settings_all_notifications_subtitle,
                onClick = {
                    openNotifications.value = Event()
                }
        ))
        add(SettingsModel.Pref(
                title = R.string.settings_all_support,
                description = R.string.settings_all_support_subtitle,
                onClick = {
                    openSupport.value = Event()
                }
        ))
        add(SettingsModel.Pref(
                title = R.string.settings_all_about,
                description = R.string.settings_all_about_subtitle,
                onClick = {
                    openAbout.value = Event()
                }
        ))
    }

    override val openAbout: MutableLiveData<Event> = MutableLiveData()
    override val openAppearance: MutableLiveData<Event> = MutableLiveData()
    override val openStatistics: MutableLiveData<Event> = MutableLiveData()
    override val openRss: MutableLiveData<Event> = MutableLiveData()
    override val openNotifications: MutableLiveData<Event> = MutableLiveData()
    override val openSupport: MutableLiveData<Event> = MutableLiveData()

    var inputs: SettingsAllViewModelInputs = this
    var outputs: SettingsAllViewModelOutputs = this
}

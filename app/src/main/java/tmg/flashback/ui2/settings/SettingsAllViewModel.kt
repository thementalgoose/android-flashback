package tmg.flashback.ui2.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.flashback.R
import tmg.flashback.ads.repository.AdsRepository
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.ui.settings.SettingsModel
import tmg.flashback.ui.settings.SettingsViewModel
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsAllViewModelInputs {

}

//endregion

//region Outputs

interface SettingsAllViewModelOutputs {
    val openAppearance: LiveData<Event>
    val openHome: LiveData<Event>
    val openRss: LiveData<Event>
    val openNotifications: LiveData<Event>
    val openSupport: LiveData<Event>
    val openAbout: LiveData<Event>
    val openAds: LiveData<Event>
}

//endregion

class SettingsAllViewModel(
        private val rssController: RSSController,
        private val adsRepository: AdsRepository
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
                title = R.string.settings_all_home,
                description = R.string.settings_all_home_subtitle,
                onClick = {
                    openHome.value = Event()
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
        if (adsRepository.allowUserConfig) {
            add(SettingsModel.Pref(
                title = R.string.settings_all_ads,
                description = R.string.settings_all_ads_subtitle,
                onClick = {
                    openAds.value = Event()
                }
            ))
        }
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
    override val openHome: MutableLiveData<Event> = MutableLiveData()
    override val openRss: MutableLiveData<Event> = MutableLiveData()
    override val openNotifications: MutableLiveData<Event> = MutableLiveData()
    override val openSupport: MutableLiveData<Event> = MutableLiveData()
    override val openAds: MutableLiveData<Event> = MutableLiveData()

    var inputs: SettingsAllViewModelInputs = this
    var outputs: SettingsAllViewModelOutputs = this
}

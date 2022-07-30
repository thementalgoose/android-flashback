package tmg.flashback.ui.settings

import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.R
import tmg.flashback.rss.RssNavigationComponent
import tmg.flashback.ads.config.AdsNavigationComponent
import tmg.flashback.ads.config.repository.AdsRepository
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.settings.SettingsNavigationComponent
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.web.WebNavigationComponent
import javax.inject.Inject

//region Inputs

interface SettingsAllViewModelInputs {

}

//endregion

//region Outputs

interface SettingsAllViewModelOutputs {
}

//endregion

@HiltViewModel
class SettingsAllViewModel @Inject constructor(
    private val rssController: RSSController,
    private val adsRepository: AdsRepository,
    private val rssNavigationComponent: RssNavigationComponent,
    private val settingsNavigationComponent: SettingsNavigationComponent,
    private val statsNavigationComponent: StatsNavigationComponent,
    private val adsNavigationComponent: AdsNavigationComponent,
    private val webNavigationComponent: WebNavigationComponent
): SettingsViewModel(), SettingsAllViewModelInputs, SettingsAllViewModelOutputs {

    override val models: List<SettingsModel> = mutableListOf<SettingsModel>().apply {
        add(SettingsModel.Header(R.string.settings_title))
        add(SettingsModel.Pref(
                title = R.string.settings_all_appearance,
                description = R.string.settings_all_appearance_subtitle,
                onClick = {
                    settingsNavigationComponent.settingsAppearance()
                }
        ))
        add(SettingsModel.Pref(
                title = R.string.settings_all_home,
                description = R.string.settings_all_home_subtitle,
                onClick = {
                    statsNavigationComponent.settingsHome()
                }
        ))
        if (rssController.enabled) {
            add(SettingsModel.Pref(
                    title = R.string.settings_all_rss,
                    description = R.string.settings_all_rss_subtitle,
                    onClick = {
                        rssNavigationComponent.settingsRSS()
                    }
            ))
        }
        add(SettingsModel.Pref(
                title = R.string.settings_all_notifications,
                description = R.string.settings_all_notifications_subtitle,
                onClick = {
                    statsNavigationComponent.settingsNotifications()
                }
        ))
        add(SettingsModel.Pref(
            title = R.string.settings_all_web_browser,
            description = R.string.settings_all_web_browser_subtitle,
            onClick = {
                webNavigationComponent.webSettings()
            }
        ))
        add(SettingsModel.Pref(
                title = R.string.settings_all_support,
                description = R.string.settings_all_support_subtitle,
                onClick = {
                    settingsNavigationComponent.settingsSupport()
                }
        ))
        if (adsRepository.allowUserConfig) {
            add(SettingsModel.Pref(
                title = R.string.settings_all_ads,
                description = R.string.settings_all_ads_subtitle,
                onClick = {
                    adsNavigationComponent.settingsAds()
                }
            ))
        }
        add(SettingsModel.Pref(
                title = R.string.settings_all_about,
                description = R.string.settings_all_about_subtitle,
                onClick = {
                    settingsNavigationComponent.settingsAbout()
                }
        ))
    }

    var inputs: SettingsAllViewModelInputs = this
    var outputs: SettingsAllViewModelOutputs = this
}

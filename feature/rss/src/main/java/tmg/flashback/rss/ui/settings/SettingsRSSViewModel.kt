package tmg.flashback.rss.ui.settings

import tmg.flashback.rss.RssNavigationComponent
import tmg.flashback.rss.R
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.ui.settings.SettingsModel
import tmg.flashback.ui.settings.SettingsViewModel

//region Inputs

interface SettingsRSSViewModelInputs {
}

//endregion

//region Outputs

interface SettingsRSSViewModelOutputs {
}

//endregion

internal class SettingsRSSViewModel (
        private val rssRepository: RSSRepository,
        private val rssNavigationComponent: RssNavigationComponent
): SettingsViewModel(), SettingsRSSViewModelInputs, SettingsRSSViewModelOutputs {

    override val models: List<SettingsModel>
        get() = listOf(
            SettingsModel.Header(R.string.settings_rss_configure),
            SettingsModel.Pref(
                title = R.string.settings_rss_configure_sources_title,
                description = R.string.settings_rss_configure_sources_description,
                onClick = {
                    rssNavigationComponent.configureRSS()
                }
            ),
            SettingsModel.Header(R.string.settings_rss_appearance_title),
            SettingsModel.SwitchPref(
                title = R.string.settings_rss_show_description_title,
                description = R.string.settings_rss_show_description_description,
                getState = { rssRepository.rssShowDescription },
                saveState = { rssRepository.rssShowDescription = it }
            )
        )

    var inputs: SettingsRSSViewModelInputs = this
    var outputs: SettingsRSSViewModelOutputs = this
}
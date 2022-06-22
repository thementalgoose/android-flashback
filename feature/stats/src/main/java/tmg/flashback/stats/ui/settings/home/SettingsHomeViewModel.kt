package tmg.flashback.stats.ui.settings.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.flashback.stats.R
import tmg.flashback.stats.repository.HomeRepository
import tmg.flashback.stats.usecases.DefaultSeasonUseCase
import tmg.flashback.ui.settings.SettingsModel
import tmg.flashback.ui.settings.SettingsViewModel
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsHomeViewModelInputs {
}

//endregion

//region Outputs

interface SettingsHomeViewModelOutputs {
}

//endregion


class SettingsHomeViewModel(
    private val defaultSeasonUseCase: DefaultSeasonUseCase,
    private val homeRepository: HomeRepository,
): SettingsViewModel(), SettingsHomeViewModelInputs, SettingsHomeViewModelOutputs {

    override val models: List<SettingsModel> get() = mutableListOf<SettingsModel>().apply {

        add(SettingsModel.Header(R.string.settings_home))
        if (defaultSeasonUseCase.isUserDefinedValueSet) {
            add(
                SettingsModel.Pref(
                title = R.string.settings_default_season_title,
                description = R.string.settings_default_season_description,
                onClick = {
                    defaultSeasonUseCase.clearDefault()
                }
            ))
        }
        add(
            SettingsModel.SwitchPref(
            title = R.string.settings_customisation_season_all_expanded_title,
            description = R.string.settings_customisation_season_all_expanded_description,
            getState = { homeRepository.showListAll },
            saveState = { homeRepository.showListAll = it }
        ))
        add(
            SettingsModel.SwitchPref(
            title = R.string.settings_customisation_season_favourited_expanded_title,
            description = R.string.settings_customisation_season_favourited_expanded_description,
            getState = { homeRepository.showListFavourited },
            saveState = { homeRepository.showListFavourited = it }
        ))
        add(SettingsModel.Header(R.string.settings_information_title))
        add(
            SettingsModel.SwitchPref(
            title = R.string.settings_information_at_top_title,
            description = R.string.settings_information_at_top_description,
            getState = { homeRepository.dataProvidedByAtTop },
            saveState = { homeRepository.dataProvidedByAtTop = it }
        ))
        add(SettingsModel.Header(R.string.settings_dashboard_title))
//        add(SettingsModel.SwitchPref(
//            title = R.string.settings_dashboard_default_to_schedule_title,
//            description = R.string.settings_dashboard_default_to_schedule_description,
//            getState = { homeRepository.dashboardDefaultToSchedule },
//            saveState = { homeRepository.dashboardDefaultToSchedule = it }
//        ))
        add(
            SettingsModel.SwitchPref(
            title = R.string.settings_dashboard_autoscroll_title,
            description = R.string.settings_dashboard_autoscroll_description,
            getState = { homeRepository.dashboardAutoscroll },
            saveState = { homeRepository.dashboardAutoscroll = it }
        ))
    }

    var inputs: SettingsHomeViewModelInputs = this
    var outputs: SettingsHomeViewModelOutputs = this
}

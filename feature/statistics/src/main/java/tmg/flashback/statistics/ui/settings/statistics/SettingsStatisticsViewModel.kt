package tmg.flashback.statistics.ui.settings.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.core.ui.settings.SettingsModel
import tmg.core.ui.settings.SettingsViewModel
import tmg.flashback.statistics.controllers.RaceController
import tmg.flashback.statistics.controllers.SeasonController
import tmg.flashback.statistics.R
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsStatisticsViewModelInputs {
}

//endregion

//region Outputs

interface SettingsStatisticsViewModelOutputs {
    val defaultSeasonChanged: LiveData<Event>
}

//endregion


class SettingsStatisticsViewModel(
        private val raceController: RaceController,
        private val seasonController: SeasonController
): SettingsViewModel(), SettingsStatisticsViewModelInputs, SettingsStatisticsViewModelOutputs {

    override val models: List<SettingsModel> = mutableListOf<SettingsModel>().apply {
        add(SettingsModel.Header(R.string.settings_statistics))
        add(SettingsModel.SwitchPref(
                title = R.string.settings_customisation_qualifying_delta_title,
                description = R.string.settings_customisation_qualifying_delta_description,
                getState = { raceController.showQualifyingDelta },
                saveState = { raceController.showQualifyingDelta = it }
        ))
        add(SettingsModel.SwitchPref(
                title = R.string.settings_customisation_qualifying_grid_penalties_title,
                description = R.string.settings_customisation_qualifying_grid_penalties_description,
                getState = { raceController.showGridPenaltiesInQualifying },
                saveState = { raceController.showGridPenaltiesInQualifying = it }
        ))
        add(SettingsModel.SwitchPref(
                title = R.string.settings_customisation_fade_dnf_title,
                description = R.string.settings_customisation_fade_dnf_description,
                getState = { raceController.fadeDNF },
                saveState = { raceController.fadeDNF = it }
        ))
        add(SettingsModel.Header(R.string.settings_home))
        if (seasonController.isUserDefinedValueSet) {
            add(SettingsModel.Pref(
                    title = R.string.settings_default_season_title,
                    description = R.string.settings_default_season_description,
                    onClick = {
                        seasonController.clearDefault()
                        defaultSeasonChanged.value = Event()
                    }
            ))
        }
        add(SettingsModel.SwitchPref(
                title = R.string.settings_customisation_season_all_expanded_title,
                description = R.string.settings_customisation_season_all_expanded_description,
                getState = { seasonController.allExpanded },
                saveState = { seasonController.allExpanded = it }
        ))
        add(SettingsModel.SwitchPref(
                title = R.string.settings_customisation_season_favourited_expanded_title,
                description = R.string.settings_customisation_season_favourited_expanded_description,
                getState = { seasonController.favouritesExpanded },
                saveState = { seasonController.favouritesExpanded = it }
        ))
    }

    var inputs: SettingsStatisticsViewModelInputs = this
    var outputs: SettingsStatisticsViewModelOutputs = this

    override val defaultSeasonChanged: MutableLiveData<Event> = MutableLiveData()

}

package tmg.flashback.statistics.ui.settings.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.flashback.statistics.R
import tmg.flashback.statistics.controllers.HomeController
import tmg.flashback.statistics.usecases.DefaultSeasonUseCase
import tmg.flashback.ui.settings.SettingsModel
import tmg.flashback.ui.settings.SettingsViewModel
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsHomeViewModelInputs {

}

//endregion

//region Outputs

interface SettingsHomeViewModelOutputs {
    val defaultSeasonChanged: LiveData<Event>
}

//endregion


class SettingsHomeViewModel(
    private val defaultSeasonUseCase: DefaultSeasonUseCase,
    private val homeController: HomeController,
): SettingsViewModel(), SettingsHomeViewModelInputs, SettingsHomeViewModelOutputs {

    override val models: List<SettingsModel> get() = mutableListOf<SettingsModel>().apply {

        add(SettingsModel.Header(R.string.settings_home))
        if (defaultSeasonUseCase.isUserDefinedValueSet) {
            add(SettingsModel.Pref(
                title = R.string.settings_default_season_title,
                description = R.string.settings_default_season_description,
                onClick = {
                    defaultSeasonUseCase.clearDefault()
                    defaultSeasonChanged.value = Event()
                }
            ))
        }
        add(SettingsModel.SwitchPref(
            title = R.string.settings_customisation_season_all_expanded_title,
            description = R.string.settings_customisation_season_all_expanded_description,
            getState = { homeController.allExpanded },
            saveState = { homeController.allExpanded = it }
        ))
        add(SettingsModel.SwitchPref(
            title = R.string.settings_customisation_season_favourited_expanded_title,
            description = R.string.settings_customisation_season_favourited_expanded_description,
            getState = { homeController.favouritesExpanded },
            saveState = { homeController.favouritesExpanded = it }
        ))
        add(SettingsModel.Header(R.string.settings_information_title))
        add(SettingsModel.SwitchPref(
            title = R.string.settings_information_at_top_title,
            description = R.string.settings_information_at_top_description,
            getState = { homeController.dataProvidedByAtTop },
            saveState = { homeController.dataProvidedByAtTop = it }
        ))
        add(SettingsModel.Header(R.string.settings_dashboard_title))
        add(SettingsModel.SwitchPref(
            title = R.string.settings_dashboard_default_to_schedule_title,
            description = R.string.settings_dashboard_default_to_schedule_description,
            getState = { homeController.dashboardDefaultToSchedule },
            saveState = { homeController.dashboardDefaultToSchedule = it }
        ))
        add(SettingsModel.SwitchPref(
            title = R.string.settings_dashboard_autoscroll_title,
            description = R.string.settings_dashboard_autoscroll_description,
            getState = { homeController.dashboardAutoscroll },
            saveState = { homeController.dashboardAutoscroll = it }
        ))
    }

    var inputs: SettingsHomeViewModelInputs = this
    var outputs: SettingsHomeViewModelOutputs = this

    override val defaultSeasonChanged: MutableLiveData<Event> = MutableLiveData()

    init {

    }

    //region Inputs

    //endregion

    //region Outputs

    //endregion
}

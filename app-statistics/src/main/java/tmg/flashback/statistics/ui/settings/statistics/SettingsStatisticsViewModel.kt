package tmg.flashback.statistics.ui.settings.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.statistics.controllers.RaceController
import tmg.flashback.statistics.controllers.SeasonController
import tmg.flashback.core.ui.BaseViewModel
import tmg.flashback.statistics.R
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsStatisticsViewModelInputs {
    fun preferenceClicked(pref: String?, value: Boolean?)
}

//endregion

//region Outputs

interface SettingsStatisticsViewModelOutputs {
    val settings: LiveData<List<AppPreferencesItem>>
    val defaultSeasonChanged: LiveData<Event>
}

//endregion


class SettingsStatisticsViewModel(
        private val raceController: RaceController,
        private val seasonController: SeasonController
): BaseViewModel(), SettingsStatisticsViewModelInputs, SettingsStatisticsViewModelOutputs {

    private val keyQualifyingDeltas: String = "QualifyingDelta"
    private val keyFadeOutDNF: String = "FadeOutDNF"
    private val keyQualifyingGridPenalty: String = "QualifyingGridPenalty"
    private val keyDefaultSeason: String = "DefaultSeason"
    private val keyBottomSheetFavourited: String = "BottomSheetFavourited"
    private val keyBottomSheetAll: String = "BottomSheetAll"

    var inputs: SettingsStatisticsViewModelInputs = this
    var outputs: SettingsStatisticsViewModelOutputs = this

    override val settings: MutableLiveData<List<AppPreferencesItem>> = MutableLiveData()
    override val defaultSeasonChanged: MutableLiveData<Event> = MutableLiveData()

    init {
        settings.value = mutableListOf<AppPreferencesItem>().apply {
            add(AppPreferencesItem.Category(R.string.settings_statistics))
            add(AppPreferencesItem.SwitchPreference(keyQualifyingDeltas, R.string.settings_customisation_qualifying_delta_title, R.string.settings_customisation_qualifying_delta_description, raceController.showQualifyingDelta))
            add(AppPreferencesItem.SwitchPreference(keyQualifyingGridPenalty, R.string.settings_customisation_qualifying_grid_penalties_title, R.string.settings_customisation_qualifying_grid_penalties_description, raceController.showGridPenaltiesInQualifying))
            add(AppPreferencesItem.SwitchPreference(keyFadeOutDNF, R.string.settings_customisation_fade_dnf_title, R.string.settings_customisation_fade_dnf_description, raceController.fadeDNF))
            add(AppPreferencesItem.Category(R.string.settings_home))
            if (seasonController.isUserDefinedValueSet) {
                add(AppPreferencesItem.Preference(keyDefaultSeason, R.string.settings_default_season_title, R.string.settings_default_season_description))
            }
            add(AppPreferencesItem.SwitchPreference(keyBottomSheetAll, R.string.settings_customisation_season_all_expanded_title, R.string.settings_customisation_season_all_expanded_description, seasonController.allExpanded))
            add(AppPreferencesItem.SwitchPreference(keyBottomSheetFavourited, R.string.settings_customisation_season_favourited_expanded_title, R.string.settings_customisation_season_favourited_expanded_description, seasonController.favouritesExpanded))
        }
    }

    //region Inputs

    override fun preferenceClicked(pref: String?, value: Boolean?) {
        when (pref) {
            keyQualifyingDeltas -> raceController.showQualifyingDelta = value ?: true
            keyFadeOutDNF -> raceController.fadeDNF = value ?: true
            keyQualifyingGridPenalty -> raceController.showGridPenaltiesInQualifying = value ?: true
            keyDefaultSeason -> {
                seasonController.clearDefault()
                defaultSeasonChanged.value = Event()
            }
            keyBottomSheetAll -> seasonController.allExpanded = value ?: true
            keyBottomSheetFavourited -> seasonController.favouritesExpanded = value ?: true
        }
    }

    //endregion

    //region Outputs

    //endregion
}

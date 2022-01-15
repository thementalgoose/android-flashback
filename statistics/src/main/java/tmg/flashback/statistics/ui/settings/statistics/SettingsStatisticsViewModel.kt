package tmg.flashback.statistics.ui.settings.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.flashback.ui.settings.SettingsModel
import tmg.flashback.ui.settings.SettingsViewModel
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
): SettingsViewModel(), SettingsStatisticsViewModelInputs, SettingsStatisticsViewModelOutputs {

    override val models: List<SettingsModel> get() = mutableListOf<SettingsModel>().apply {

    }

    var inputs: SettingsStatisticsViewModelInputs = this
    var outputs: SettingsStatisticsViewModelOutputs = this

    override val defaultSeasonChanged: MutableLiveData<Event> = MutableLiveData()

}

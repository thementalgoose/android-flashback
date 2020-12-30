package tmg.flashback.dashboard.season

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.flashback.base.BaseViewModel
import tmg.flashback.home.HomeMenuItem
import tmg.utilities.lifecycle.Event

//region Inputs

interface SeasonViewModelInputs {
    fun clickMenu()
    fun clickSettings()
    fun clickItem(item: HomeMenuItem)
    fun selectSeason(season: Int)
}

//endregion

//region Outputs

interface SeasonViewModelOutputs {
    val openMenu: LiveData<Event>
    val openSettings: LiveData<Event>
}

//endregion


class SeasonViewModel: BaseViewModel(), SeasonViewModelInputs, SeasonViewModelOutputs {

    override val openMenu: MutableLiveData<Event> = MutableLiveData()
    override val openSettings: MutableLiveData<Event> = MutableLiveData()

    var inputs: SeasonViewModelInputs = this
    var outputs: SeasonViewModelOutputs = this

    init {

    }

    //region Inputs

    override fun clickMenu() {
        openMenu.value = Event()
    }

    override fun clickSettings() {
        openSettings.value = Event()
    }

    override fun clickItem(item: HomeMenuItem) {

    }

    override fun selectSeason(season: Int) {

    }

    //endregion

    //region Outputs

    //endregion
}

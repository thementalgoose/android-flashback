package tmg.flashback.ui.dashboard.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

//region Inputs

interface MenuViewModelInputs {
//    fun clickSeason(season: Int)
}

//endregion

//region Outputs

interface MenuViewModelOutputs {
//    val buttons: LiveData<List<*>>
//    val season: LiveData<List<*>>
}

//endregion

class MenuViewModel: ViewModel(), MenuViewModelInputs, MenuViewModelOutputs {

    val inputs: MenuViewModelInputs = this
    val outputs: MenuViewModelOutputs = this



}
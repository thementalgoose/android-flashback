package tmg.flashback.ui.dashboard.menu

import androidx.lifecycle.ViewModel

//region Inputs

interface MenuViewModelInputs {

}

//endregion

//region Outputs

interface MenuViewModelOutputs {

}

//endregion

class MenuViewModel: ViewModel(), MenuViewModelInputs, MenuViewModelOutputs {

    val inputs: MenuViewModelInputs = this
    val outputs: MenuViewModelOutputs = this

}
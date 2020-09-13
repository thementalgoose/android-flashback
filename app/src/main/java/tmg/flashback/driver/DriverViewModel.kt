package tmg.flashback.driver

import tmg.flashback.base.BaseViewModel
import tmg.flashback.di.async.ScopeProvider

//region Inputs

interface DriverViewModelInputs {

}

//endregion

//region Outputs

interface DriverViewModelOutputs {

}

//endregion

class DriverViewModel(
    executionScope: ScopeProvider
): BaseViewModel(executionScope), DriverViewModelInputs, DriverViewModelOutputs {

    var inputs: DriverViewModelInputs = this
    var outputs: DriverViewModelOutputs = this

    init {

    }

    //region Inputs

    //endregion

    //region Outputs

    //endregion
}
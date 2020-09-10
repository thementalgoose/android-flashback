package tmg.flashback.driver.comparison

import tmg.flashback.base.BaseViewModel
import tmg.flashback.driver.DriverViewModelInputs
import tmg.flashback.driver.DriverViewModelOutputs

//region Inputs

interface DriverComparisonViewModelInputs {

}

//endregion

//region Outputs

interface DriverComparisonViewModelOutputs {

}

//endregion


class DriverComparisonViewModel: BaseViewModel(), DriverComparisonViewModelInputs, DriverComparisonViewModelOutputs {

    var inputs: DriverComparisonViewModelInputs = this
    var outputs: DriverComparisonViewModelOutputs = this

    init {

    }

    //region Inputs

    //endregion

    //region Outputs

    //endregion
}

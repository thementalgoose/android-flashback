package tmg.flashback.driver.comparison

import tmg.flashback.base.BaseViewModel
import tmg.flashback.repo.ScopeProvider

//region Inputs

interface DriverComparisonViewModelInputs {

}

//endregion

//region Outputs

interface DriverComparisonViewModelOutputs {

}

//endregion


class DriverComparisonViewModel(
    scopeProvider: ScopeProvider
): BaseViewModel(scopeProvider), DriverComparisonViewModelInputs, DriverComparisonViewModelOutputs {

    var inputs: DriverComparisonViewModelInputs = this
    var outputs: DriverComparisonViewModelOutputs = this

    init {

    }

    //region Inputs

    //endregion

    //region Outputs

    //endregion
}

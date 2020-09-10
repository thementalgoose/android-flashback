package tmg.flashback.driver.overview

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import tmg.flashback.base.BaseViewModel

//region Inputs

interface DriverOverviewViewModelInputs {

}

//endregion

//region Outputs

interface DriverOverviewViewModelOutputs {

}

//endregion


@FlowPreview
@ExperimentalCoroutinesApi
class DriverOverviewViewModel: BaseViewModel(), DriverOverviewViewModelInputs, DriverOverviewViewModelOutputs {

    var inputs: DriverOverviewViewModelInputs = this
    var outputs: DriverOverviewViewModelOutputs = this

    init {

    }

    //region Inputs

    //endregion

    //region Outputs

    //endregion
}

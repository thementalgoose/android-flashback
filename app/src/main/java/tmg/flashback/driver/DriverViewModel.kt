package tmg.flashback.driver

import tmg.flashback.base.BaseViewModel

//region Inputs

interface DriverViewModelInputs {
    fun setup(driverId: String)
}

//endregion

//region Outputs

interface DriverViewModelOutputs {

}

//endregion

class DriverViewModel: BaseViewModel(), DriverViewModelInputs, DriverViewModelOutputs {

    var inputs: DriverViewModelInputs = this
    var outputs: DriverViewModelOutputs = this

    init {

    }

    //region Inputs

    override fun setup(driverId: String) {

    }

    //endregion

    //region Outputs

    //endregion
}
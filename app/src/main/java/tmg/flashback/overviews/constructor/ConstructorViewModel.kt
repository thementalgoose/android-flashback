package tmg.flashback.overviews.constructor

import tmg.flashback.base.BaseViewModel
import tmg.flashback.di.async.ScopeProvider

//region Inputs

interface ConstructorViewModelInputs {

}

//endregion

//region Outputs

interface ConstructorViewModelOutputs {

}

//endregion


class ConstructorViewModel(
    scopeProvider: ScopeProvider
): BaseViewModel(scopeProvider), ConstructorViewModelInputs, ConstructorViewModelOutputs {

    var inputs: ConstructorViewModelInputs = this
    var outputs: ConstructorViewModelOutputs = this

    init {

    }

    //region Inputs

    //endregion

    //region Outputs

    //endregion
}

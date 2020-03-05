package tmg.f1stats.home.season

import tmg.f1stats.base.BaseViewModel

//region Inputs

interface SeasonViewModelInputs {

}

//endregion

//region Outputs

interface SeasonViewModelOutputs {

}

//endregion

class SeasonViewModel: BaseViewModel(), SeasonViewModelInputs, SeasonViewModelOutputs {

    var inputs: SeasonViewModelInputs = this
    var outputs: SeasonViewModelOutputs = this
    
    init { 
        
    }
    
    //region Inputs
    
    //endregion
    
    //region Outputs
    
    //endregion
}
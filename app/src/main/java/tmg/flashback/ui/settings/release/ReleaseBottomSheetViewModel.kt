package tmg.flashback.ui.settings.release

import tmg.flashback.controllers.ReleaseNotesController
import tmg.flashback.ui.base.BaseViewModel

//region Inputs

interface ReleaseBottomSheetViewModelInputs {

}

//endregion

//region Outputs

interface ReleaseBottomSheetViewModelOutputs {

}

//endregion


class ReleaseBottomSheetViewModel(
        private val releaseNotesController: ReleaseNotesController
): BaseViewModel(), ReleaseBottomSheetViewModelInputs, ReleaseBottomSheetViewModelOutputs {

    var inputs: ReleaseBottomSheetViewModelInputs = this
    var outputs: ReleaseBottomSheetViewModelOutputs = this

    init {

    }

    //region Inputs

    //endregion

    //region Outputs

    //endregion
}

package tmg.flashback.upnext.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.Timestamp

//region Inputs

interface UpNextViewModelInputs {

}

//endregion

//region Outputs

interface UpNextViewModelOutputs {
    val content: LiveData<List<UpNextBreakdownModel>>
}

//endregion


class UpNextViewModel: ViewModel(), UpNextViewModelInputs, UpNextViewModelOutputs {

    override val content: MutableLiveData<List<UpNextBreakdownModel>> = MutableLiveData()

    var inputs: UpNextViewModelInputs = this
    var outputs: UpNextViewModelOutputs = this

    init {
        content.value = listOf(
            UpNextBreakdownModel.Day("Saturday 18th June"),
            UpNextBreakdownModel.Item("Qualifying", Timestamp(LocalDate.of(2020,1,1))),
            UpNextBreakdownModel.Day("Sunday 19th June"),
            UpNextBreakdownModel.Item("Race", Timestamp(LocalDate.of(2020,1,1))),
        )
    }

    //region Inputs

    //endregion

    //region Outputs

    //endregion
}

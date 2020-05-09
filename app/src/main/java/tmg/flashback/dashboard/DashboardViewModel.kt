package tmg.flashback.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.flashback.base.BaseViewModel
import tmg.flashback.currentYear
import tmg.flashback.dashboard.year.DashboardYearItem
import tmg.flashback.minimumSupportedYear

//region Inputs

interface DashboardViewModelInputs {

}

//endregion

//region Outputs

interface DashboardViewModelOutputs {
    val years: MutableLiveData<List<DashboardYearItem>>
}

//endregion

class DashboardViewModel: BaseViewModel(), DashboardViewModelInputs, DashboardViewModelOutputs {

    override val years: MutableLiveData<List<DashboardYearItem>> = MutableLiveData()

    var inputs: DashboardViewModelInputs = this
    var outputs: DashboardViewModelOutputs = this

    init {
        years.value = List((currentYear + 1) - minimumSupportedYear) {
            DashboardYearItem(minimumSupportedYear + it)
        }
    }

    //region Inputs

    //endregion

    //region Outputs

    //endregion
}
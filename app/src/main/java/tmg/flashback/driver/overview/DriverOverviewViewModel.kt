package tmg.flashback.driver.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import tmg.flashback.base.BaseViewModel
import tmg.flashback.repo.models.stats.DriverOverview

//region Inputs

interface DriverOverviewViewModelInputs {
    fun setup(driverId: String)
}

//endregion

//region Outputs

interface DriverOverviewViewModelOutputs {
    val list: LiveData<List<DriverOverviewItem>>
}

//endregion


@FlowPreview
@ExperimentalCoroutinesApi
class DriverOverviewViewModel: BaseViewModel(), DriverOverviewViewModelInputs, DriverOverviewViewModelOutputs {

    var inputs: DriverOverviewViewModelInputs = this
    var outputs: DriverOverviewViewModelOutputs = this

    override val list: LiveData<List<DriverOverviewItem>>
        get() = MutableLiveData()

    init {

    }

    //region Inputs

    override fun setup(driverId: String) {

    }

    //endregion

    //region Outputs

    //endregion
}

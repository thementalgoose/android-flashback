package tmg.flashback.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import tmg.flashback.base.BaseViewModel
import tmg.flashback.di.device.BuildConfigManager
import tmg.flashback.repo.db.DataRepository
import tmg.utilities.lifecycle.Event

//region Inputs

interface DashboardViewModelInputs {

}

//endregion

//region Outputs

interface DashboardViewModelOutputs {
    val openAppLockout: LiveData<Event>
    val openReleaseNotes: LiveData<Event>
}

//endregion


class DashboardViewModel(
    private val dataRepository: DataRepository,
    private val buildConfigManager: BuildConfigManager
): BaseViewModel(), DashboardViewModelInputs, DashboardViewModelOutputs {

    override val openAppLockout: LiveData<Event> = dataRepository
        .appLockout()
        .map {
            if (it?.show == true && buildConfigManager.shouldLockoutBasedOnVersion(it.version)) {
                Event()
            } else {
                null
            }
        }
        .filterNotNull()
        .asLiveData(viewModelScope.coroutineContext)

    override val openReleaseNotes: MutableLiveData<Event> = MutableLiveData()

    var inputs: DashboardViewModelInputs = this
    var outputs: DashboardViewModelOutputs = this

    init {

    }

    //region Inputs

    //endregion

    //region Outputs

    //endregion
}

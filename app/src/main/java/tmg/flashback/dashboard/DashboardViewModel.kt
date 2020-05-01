package tmg.flashback.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import tmg.flashback.base.BaseViewModel
import tmg.flashback.repo.db.DataDB
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.models.AppLockout
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

//region Inputs

interface DashboardViewModelInputs {

}

//endregion

//region Outputs

interface DashboardViewModelOutputs {

    val showAppLockoutMessage: LiveData<DataEvent<AppLockout>>
    val showReleaseNotes: LiveData<Event>
}

//endregion

class DashboardViewModel(
    dataDB: DataDB,
    prefsDB: PrefsDB
): BaseViewModel(), DashboardViewModelInputs, DashboardViewModelOutputs {

    var inputs: DashboardViewModelInputs = this
    var outputs: DashboardViewModelOutputs = this

    override val showAppLockoutMessage: LiveData<DataEvent<AppLockout>> = dataDB
        .appLockout()
        .filter { it != null && it.show }
        .map { DataEvent(it!!) }
        .asLiveData(viewModelScope.coroutineContext)

    override val showReleaseNotes: LiveData<Event> = liveData {
        if (prefsDB.isCurrentAppVersionNew) {
            emit(Event())
        }
    }

    init {

    }

    //region Inputs

    //endregion

    //region Outputs

    //endregion
}
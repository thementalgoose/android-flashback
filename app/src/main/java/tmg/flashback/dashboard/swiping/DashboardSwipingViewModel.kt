package tmg.flashback.dashboard.swiping

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import tmg.flashback.base.BaseViewModel
import tmg.flashback.repo.db.DataDB
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.models.AppBanner
import tmg.flashback.repo.models.AppLockout
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

//region Inputs

interface DashboardSwipingViewModelInputs {

}

//endregion

//region Outputs

interface DashboardSwipingViewModelOutputs {

    val showAppBanner: LiveData<DataEvent<AppBanner>>
    val showAppLockoutMessage: LiveData<DataEvent<AppLockout>>
    val showReleaseNotes: LiveData<Event>
}

//endregion

class DashboardSwipingViewModel(
    dataDB: DataDB,
    prefsDB: PrefsDB
): BaseViewModel(), DashboardSwipingViewModelInputs,
    DashboardSwipingViewModelOutputs {

    var inputs: DashboardSwipingViewModelInputs = this
    var outputs: DashboardSwipingViewModelOutputs = this

    override val showAppLockoutMessage: LiveData<DataEvent<AppLockout>> = dataDB
        .appLockout()
        .filter { it != null && it.show }
        .map { DataEvent(it!!) }
        .asLiveData(viewModelScope.coroutineContext)

    override val showAppBanner: LiveData<DataEvent<AppBanner>> = dataDB
        .appBanner()
        .filter { it != null && it.show }
        .map { DataEvent(it!!) }
        .asLiveData(viewModelScope.coroutineContext)

    override val showReleaseNotes: LiveData<Event> = liveData {
        if (prefsDB.shouldShowReleaseNotes) {
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
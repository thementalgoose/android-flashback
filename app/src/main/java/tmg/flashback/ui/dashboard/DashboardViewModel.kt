package tmg.flashback.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import tmg.flashback.ui.base.BaseViewModel
import tmg.flashback.controllers.ReleaseNotesController
import tmg.flashback.managers.buildconfig.BuildConfigManager
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
        private val buildConfigManager: BuildConfigManager,
        private val releaseNotesController: ReleaseNotesController
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
        if (releaseNotesController.pendingReleaseNotes) {
            openReleaseNotes.value = Event()
        }
    }

    //region Inputs

    //endregion

    //region Outputs

    //endregion
}

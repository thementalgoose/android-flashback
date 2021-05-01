package tmg.flashback.statistics.ui.dashboard

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tmg.configuration.controllers.ConfigController
import tmg.flashback.core.ui.BaseViewModel
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.data.db.DataRepository
import tmg.flashback.statistics.BuildConfig
import tmg.flashback.statistics.extensions.updateAllWidgets
import tmg.utilities.lifecycle.Event

//region Inputs

interface DashboardViewModelInputs {

}

//endregion

//region Outputs

interface DashboardViewModelOutputs {
    val openAppLockout: LiveData<Event>
    val openReleaseNotes: LiveData<Event>

    val appConfigSynced: LiveData<Event>
}

//endregion

class DashboardViewModel(
    private val applicationContext: Context,
    private val dataRepository: DataRepository,
    private val buildConfigManager: BuildConfigManager,
    private val configurationController: ConfigController
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
    override val appConfigSynced: MutableLiveData<Event> = MutableLiveData()

    var inputs: DashboardViewModelInputs = this
    var outputs: DashboardViewModelOutputs = this

    init {
        viewModelScope.launch {
            configurationController.fetch()
            val activate = configurationController.applyPending()
            if (activate) {
                if (BuildConfig.DEBUG) {
                    Log.i("Flashback", "Remote config change detected")
                }
                appConfigSynced.value = Event()
                applicationContext.updateAllWidgets()
            }
        }
    }

    //region Inputs

    //endregion

    //region Outputs

    //endregion
}

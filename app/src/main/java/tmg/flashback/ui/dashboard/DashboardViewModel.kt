package tmg.flashback.ui.dashboard

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import tmg.flashback.configuration.controllers.ConfigController
import tmg.flashback.common.controllers.ReleaseNotesController
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.statistics.BuildConfig
import tmg.flashback.statistics.controllers.ScheduleController
import tmg.flashback.statistics.extensions.updateAllWidgets
import tmg.flashback.statistics.workmanager.NotificationScheduler
import tmg.flashback.statistics.workmanager.NotificationSchedulerProvider
import tmg.utilities.lifecycle.Event

//region Inputs

interface DashboardViewModelInputs {
    fun clickSearch()
}

//endregion

//region Outputs

interface DashboardViewModelOutputs {
    val openReleaseNotes: LiveData<Event>
    val openSearch: LiveData<Event>

    val appConfigSynced: LiveData<Event>
}

//endregion

class DashboardViewModel(
    applicationContext: Context,
    private val notificationSchedulerProvider: NotificationSchedulerProvider,
    private val configurationController: ConfigController,
    private val releaseNotesController: ReleaseNotesController,
): ViewModel(), DashboardViewModelInputs, DashboardViewModelOutputs {

    override val openReleaseNotes: MutableLiveData<Event> = MutableLiveData()
    override val appConfigSynced: MutableLiveData<Event> = MutableLiveData()
    override val openSearch: MutableLiveData<Event> = MutableLiveData()

    var inputs: DashboardViewModelInputs = this
    var outputs: DashboardViewModelOutputs = this

    init {
        viewModelScope.launch {
            configurationController.fetch()
            val activate = configurationController.applyPending()
            if (BuildConfig.DEBUG) {
                Log.i("Flashback", "Remote config change detected $activate")
            }
            if (activate) {
                appConfigSynced.value = Event()
                notificationSchedulerProvider.schedule()
                applicationContext.updateAllWidgets()
            }
        }

        if (releaseNotesController.pendingReleaseNotes) {
            openReleaseNotes.value = Event()
        }
    }

    //region Inputs

    override fun clickSearch() {
        openSearch.value = Event()
    }

    //endregion

    //region Outputs

    //endregion
}

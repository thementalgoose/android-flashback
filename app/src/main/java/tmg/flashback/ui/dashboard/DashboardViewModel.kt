package tmg.flashback.ui.dashboard

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import tmg.configuration.controllers.ConfigController
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import tmg.common.controllers.ReleaseNotesController
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.data.db.DataRepository
import tmg.flashback.statistics.BuildConfig
import tmg.flashback.upnext.controllers.UpNextController
import tmg.flashback.upnext.extensions.updateAllWidgets
import tmg.utilities.lifecycle.Event

//region Inputs

interface DashboardViewModelInputs {
    fun clickSearch()
}

//endregion

//region Outputs

interface DashboardViewModelOutputs {
    val openAppLockout: LiveData<Event>
    val openUpNextNotificationOnboarding: LiveData<Event>
    val openReleaseNotes: LiveData<Event>
    val openSearch: LiveData<Event>

    val showUpNext: LiveData<Boolean>

    val appConfigSynced: LiveData<Event>
}

//endregion

class DashboardViewModel(
    private val applicationContext: Context,
    private val dataRepository: DataRepository,
    private val upNextController: UpNextController,
    private val buildConfigManager: BuildConfigManager,
    private val configurationController: ConfigController,
    private val releaseNotesController: ReleaseNotesController,
): ViewModel(), DashboardViewModelInputs, DashboardViewModelOutputs {

    override val openAppLockout: LiveData<Event> = dataRepository
        .appLockout()
        .filterNotNull()
        .filter { it.showLockout(buildConfigManager.versionCode) }
        .map { Event() }
        .filterNotNull()
        .asLiveData(viewModelScope.coroutineContext)

    override val openReleaseNotes: MutableLiveData<Event> = MutableLiveData()
    override val openUpNextNotificationOnboarding: MutableLiveData<Event> = MutableLiveData()
    override val appConfigSynced: MutableLiveData<Event> = MutableLiveData()
    override val showUpNext: MutableLiveData<Boolean> = MutableLiveData()
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
                upNextController.scheduleNotifications()
                applicationContext.updateAllWidgets()
            }
        }

        showUpNext.value = upNextController.getNextEvent() != null

        if (upNextController.shouldShowNotificationOnboarding) {
            openUpNextNotificationOnboarding.value = Event()
        } else if (releaseNotesController.pendingReleaseNotes) {
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

package tmg.flashback.ui2.dashboard

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tmg.flashback.configuration.usecases.ApplyConfigUseCase
import tmg.flashback.configuration.usecases.FetchConfigUseCase
import tmg.flashback.releasenotes.usecases.NewReleaseNotesUseCase
import tmg.flashback.statistics.BuildConfig
import tmg.flashback.statistics.controllers.HomeController
import tmg.flashback.statistics.extensions.updateAllWidgets
import tmg.flashback.statistics.workmanager.WorkerProvider
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

    val defaultToSchedule: Boolean
}

//endregion

class DashboardViewModel(
    applicationContext: Context,
    private val workerProvider: WorkerProvider,
    private val fetchConfigUseCase: FetchConfigUseCase,
    private val applyConfigUseCase: ApplyConfigUseCase,
    private val homeController: HomeController,
    private val newReleaseNotesUseCase: NewReleaseNotesUseCase,
): ViewModel(), DashboardViewModelInputs, DashboardViewModelOutputs {

    override val openReleaseNotes: MutableLiveData<Event> = MutableLiveData()
    override val appConfigSynced: MutableLiveData<Event> = MutableLiveData()
    override val openSearch: MutableLiveData<Event> = MutableLiveData()

    override val defaultToSchedule: Boolean get() = homeController.dashboardDefaultToSchedule

    var inputs: DashboardViewModelInputs = this
    var outputs: DashboardViewModelOutputs = this

    init {
        viewModelScope.launch {
            fetchConfigUseCase.fetch()
            val activate = applyConfigUseCase.apply()
            if (BuildConfig.DEBUG) {
                Log.i("Dashboard", "Remote config change detected $activate")
            }
            if (activate) {
                appConfigSynced.postValue(Event())
                workerProvider.schedule()
                applicationContext.updateAllWidgets()
            }
        }

        if (newReleaseNotesUseCase.getNotes().isNotEmpty()) {
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

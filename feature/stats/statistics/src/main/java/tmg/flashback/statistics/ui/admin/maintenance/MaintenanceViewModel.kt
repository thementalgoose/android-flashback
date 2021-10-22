package tmg.flashback.statistics.ui.admin.maintenance

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.data.db.DataRepository
import tmg.flashback.data.models.AppLockout
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

/**
 * To lockout the app
 *
 * - version must be greater than or equal to the build number to lockout
 * - show = true
 *
 * If any of these conditions are not met (including show = true, version = null) then lockout won't happen!
 */

//region Inputs

interface MaintenanceViewModelInputs {
    fun clickLink()
}

//endregion

//region Outputs

interface MaintenanceViewModelOutputs {
    val data: LiveData<Pair<String, String>> // title, message
    val showLink: LiveData<Pair<String, String>> // linkText, link

    val returnToHome: LiveData<Event>
    val openLinkEvent: LiveData<DataEvent<String>>
}

//endregion

class MaintenanceViewModel(
    private val dataRepository: DataRepository,
    private val buildConfigProvider: BuildConfigManager
): ViewModel(), MaintenanceViewModelInputs, MaintenanceViewModelOutputs {

    private val clickLinkEvent: ConflatedBroadcastChannel<DataEvent<String>> = ConflatedBroadcastChannel()

    private val appLockedData: Flow<AppLockout?> = dataRepository.appLockout()

    override val data: LiveData<Pair<String, String>> = appLockedData // Title, Message
        .filterNotNull()
        .map { Pair(it.title, it.message) }
        .asLiveData(viewModelScope.coroutineContext)

    override val showLink: LiveData<Pair<String, String>> = appLockedData // linkText, link
        .map { Pair(it?.linkText ?: "", it?.link ?: "") }
        .asLiveData(viewModelScope.coroutineContext)

    override val openLinkEvent: LiveData<DataEvent<String>> = clickLinkEvent
        .asFlow()
        .asLiveData(viewModelScope.coroutineContext)

    override val returnToHome: LiveData<Event> = appLockedData
        .filter {
            it == null || !it.showLockout(buildConfigProvider.versionCode)
        }
        .map { Event() }
        .asLiveData(viewModelScope.coroutineContext)

    var inputs: MaintenanceViewModelInputs = this
    var outputs: MaintenanceViewModelOutputs = this

    //region Inputs

    override fun clickLink() {
        val valueInShowLink = showLink.value
        valueInShowLink?.second?.let {
            clickLinkEvent.offer(DataEvent(it))
        }
    }

    //endregion
}
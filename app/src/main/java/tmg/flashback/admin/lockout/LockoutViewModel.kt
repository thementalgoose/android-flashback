package tmg.flashback.admin.lockout

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import tmg.flashback.base.BaseViewModel
import tmg.flashback.isValidVersion
import tmg.flashback.repo.db.stats.DataDB
import tmg.flashback.repo.models.AppLockout
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

//region Inputs

interface LockoutViewModelInputs {
    fun clickLink()
}

//endregion

//region Outputs

interface LockoutViewModelOutputs {
    val data: LiveData<Pair<String, String>>
    val showLink: LiveData<String>

    val returnToHome: LiveData<Event>
    val openLinkEvent: LiveData<DataEvent<String>>
}

//endregion

class LockoutViewModel(
    dataDB: DataDB,
    coroutineDispatcher: CoroutineDispatcher
): BaseViewModel(), LockoutViewModelInputs, LockoutViewModelOutputs {

    private val clickLinkEvent: ConflatedBroadcastChannel<Unit> = ConflatedBroadcastChannel()

    private var appLocked: AppLockout? = null

    private val appLockedData: Flow<AppLockout> = dataDB.appLockout()
        .filter { it != null }
        .map {
            appLocked = it
            it!!
        }
        .flowOn(coroutineDispatcher)

    override val data: LiveData<Pair<String, String>> = appLockedData // Title, Message
        .map { Pair(it.title, it.message) }
        .asLiveData(viewModelScope.coroutineContext)

    override val showLink: LiveData<String> = appLockedData
        .map { it.linkText ?: "" }
        .asLiveData(viewModelScope.coroutineContext)

    override val openLinkEvent: LiveData<DataEvent<String>> = clickLinkEvent
        .asFlow()
        .map { appLocked?.link }
        .filter { it != null }
        .map { DataEvent(it!!) }
        .asLiveData(viewModelScope.coroutineContext)

    override val returnToHome: LiveData<Event> = appLockedData
        .map { it.show && isValidVersion(it.version) }
        .filter { !it }
        .map { Event() }
        .asLiveData(viewModelScope.coroutineContext)

    var inputs: LockoutViewModelInputs = this
    var outputs: LockoutViewModelOutputs = this

    //region Inputs

    override fun clickLink() {
        clickLinkEvent.offer(Unit)
    }

    //endregion
}
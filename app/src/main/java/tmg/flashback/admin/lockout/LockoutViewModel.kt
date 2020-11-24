package tmg.flashback.admin.lockout

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import tmg.flashback.base.BaseViewModel
import tmg.flashback.repo.db.stats.DataDB
import tmg.flashback.repo.models.AppLockout
import tmg.flashback.di.device.BuildConfigProvider
import tmg.flashback.repo.ScopeProvider
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

interface LockoutViewModelInputs {
    fun clickLink(maintenanceLink: String)
}

//endregion

//region Outputs

interface LockoutViewModelOutputs {
    val data: LiveData<Pair<String, String>> // title, message
    val showLink: LiveData<Pair<String, String>> // linkText, link

    val returnToHome: LiveData<Event>
    val openLinkEvent: LiveData<DataEvent<String>>
}

//endregion

@Suppress("EXPERIMENTAL_API_USAGE")
class LockoutViewModel(
    dataDB: DataDB,
    scopeProvider: ScopeProvider,
    buildConfigProvider: BuildConfigProvider
): BaseViewModel(scopeProvider), LockoutViewModelInputs, LockoutViewModelOutputs {

    private val clickLinkEvent: ConflatedBroadcastChannel<DataEvent<String>> = ConflatedBroadcastChannel()

    private var appLocked: AppLockout? = null

    private val appLockedData: Flow<AppLockout?> = dataDB.appLockout()
        .map {
            appLocked = it
            it
        }

    override val data: LiveData<Pair<String, String>> = appLockedData // Title, Message
        .filterNotNull()
        .map { Pair(it.title, it.message) }
        .asLiveData(scope.coroutineContext)

    override val showLink: LiveData<Pair<String, String>> = appLockedData
        .map { Pair(it?.linkText ?: "", it?.link ?: "") }
        .asLiveData(scope.coroutineContext)

    override val openLinkEvent: LiveData<DataEvent<String>> = clickLinkEvent
        .asFlow()
        .asLiveData(scope.coroutineContext)

    override val returnToHome: LiveData<Event> = appLockedData
        .filter { it?.show != true || !buildConfigProvider.shouldLockoutBasedOnVersion(it.version)}
        .map { Event() }
        .asLiveData(scope.coroutineContext)

    var inputs: LockoutViewModelInputs = this
    var outputs: LockoutViewModelOutputs = this

    //region Inputs

    override fun clickLink(maintenanceLink: String) {
        clickLinkEvent.offer(DataEvent(maintenanceLink))
    }

    //endregion
}
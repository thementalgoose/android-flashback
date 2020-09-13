package tmg.flashback.driver

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import tmg.flashback.base.BaseViewModel
import tmg.flashback.di.async.ScopeProvider
import tmg.flashback.repo.db.stats.DriverDB

//region Inputs

interface DriverViewModelInputs {
    fun setup(driverId: String)
}

//endregion

//region Outputs

interface DriverViewModelOutputs {
    val seasons: LiveData<List<Int>>
}

//endregion

@FlowPreview
@ExperimentalCoroutinesApi
class DriverViewModel(
    driverDB: DriverDB,
    scopeProvider: ScopeProvider
): BaseViewModel(scopeProvider), DriverViewModelInputs, DriverViewModelOutputs {

    var inputs: DriverViewModelInputs = this
    var outputs: DriverViewModelOutputs = this

    private val driverId: ConflatedBroadcastChannel<String> = ConflatedBroadcastChannel()

    override val seasons: LiveData<List<Int>> = driverId
        .asFlow()
        .flatMapLatest { driverDB.getDriverOverview(it) }
        .map {
            when (it) {
                null -> emptyList()
                else -> it.standings
                    .map { it.season }
                    .sortedByDescending { it }
            }
        }
        .asLiveData(viewModelScope.coroutineContext)

    init {

    }

    //region Inputs

    override fun setup(driverId: String) {
        if (this.driverId.valueOrNull != driverId) {
            this.driverId.offer(driverId)
        }
    }

    //endregion
}
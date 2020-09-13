package tmg.flashback.circuit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import tmg.flashback.base.BaseViewModel
import tmg.flashback.circuit.list.CircuitItem
import tmg.flashback.extensions.circuitIcon
import tmg.flashback.repo.db.stats.CircuitDB
import tmg.flashback.repo.models.stats.Circuit
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.shared.SyncDataItem
import tmg.flashback.utils.getScope
import tmg.utilities.extensions.then
import tmg.utilities.lifecycle.DataEvent
import kotlin.coroutines.CoroutineContext

//region Inputs

interface CircuitInfoViewModelInputs {
    fun circuitId(circuitId: String)
    fun clickShowOnMap()
    fun clickWikipedia()
}

//endregion

//region Outputs

interface CircuitInfoViewModelOutputs {
    val list: LiveData<List<CircuitItem>>
    val circuitName: LiveData<String>
    val isLoading: MutableLiveData<Boolean>

    val showOnMap: MutableLiveData<DataEvent<String>>
    val showWikipedia: MutableLiveData<DataEvent<String>>
}

//endregion

@FlowPreview
@ExperimentalCoroutinesApi
class CircuitInfoViewModel(
    private val circuitDB: CircuitDB,
    private val connectivityManager: ConnectivityManager,
    scope: CoroutineScope?
) : BaseViewModel(), CircuitInfoViewModelInputs, CircuitInfoViewModelOutputs {

    private val vmScope = getScope(scope)

    private var circuitLat: Double? = null
    private var circuitLng: Double? = null
    private var wikipedia: String? = null
    private var name: String? = null

    private val circuitIdentifier: ConflatedBroadcastChannel<String> = ConflatedBroadcastChannel()

    private val circuit: Flow<Circuit?> = circuitIdentifier
        .asFlow()
        .flatMapLatest { circuitDB.getCircuit(it) }

    override val showOnMap: MutableLiveData<DataEvent<String>> = MutableLiveData()

    override val showWikipedia: MutableLiveData<DataEvent<String>> = MutableLiveData()

    override val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    override val circuitName: LiveData<String> = circuit
        .map { it?.name }
        .filterNotNull()
        .asLiveData(vmScope.coroutineContext)

    override val list: LiveData<List<CircuitItem>> = circuit
        .map {
            name = it?.name
            circuitLat = it?.locationLat
            circuitLng = it?.locationLng
            wikipedia = it?.wikiUrl
            when {
                it == null && !connectivityManager.isConnected -> {
                    return@map listOf<CircuitItem>(CircuitItem.ErrorItem(SyncDataItem.NoNetwork))
                }
                it == null -> {
                    return@map listOf<CircuitItem>(CircuitItem.ErrorItem(SyncDataItem.InternalError))
                }
                else -> {
                    val list = mutableListOf<CircuitItem>()
                    it.circuitIcon?.let {
                        list.add(CircuitItem.TrackImage(it))
                    }
                    list.add(CircuitItem.CircuitInfo(it))
                    list.addAll(it.results
                        .map { result ->
                            CircuitItem.Race(
                                name = result.name,
                                season = result.season,
                                round = result.round,
                                date = result.date,
                                time = result.time
                            )
                        }
                        .sortedByDescending { it.season * 1000 + it.round }
                    )
                    return@map list
                }
            }
        }
        .then {
            isLoading.postValue(false)
        }
        .asLiveData(vmScope.coroutineContext)

    var inputs: CircuitInfoViewModelInputs = this
    var outputs: CircuitInfoViewModelOutputs = this

    init {
        isLoading.postValue(true)
    }

    //region Inputs

    override fun circuitId(circuitId: String) {
        circuitIdentifier.offer(circuitId)
    }

    override fun clickShowOnMap() {
        val mapsIntent = "geo:0,0?q=$circuitLat,$circuitLng ($name)"
        showOnMap.postValue(DataEvent(mapsIntent))
    }

    override fun clickWikipedia() {

    }

    //endregion
}
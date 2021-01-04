package tmg.flashback.ui.circuit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import tmg.flashback.ui.base.BaseViewModel
import tmg.flashback.extensions.circuitIcon
import tmg.flashback.managers.networkconnectivity.NetworkConnectivityManager
import tmg.flashback.repo.db.stats.CircuitRepository
import tmg.flashback.repo.models.stats.Circuit
import tmg.flashback.ui.shared.sync.SyncDataItem
import tmg.utilities.extensions.then
import tmg.utilities.lifecycle.DataEvent

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
    val isLoading: LiveData<Boolean>

    val goToMap: LiveData<DataEvent<Pair<String, String>>> // Maps URI, lat / lng
    val goToWikipediaPage: MutableLiveData<DataEvent<String>>
}

//endregion

@Suppress("EXPERIMENTAL_API_USAGE")
class CircuitInfoViewModel(
        private val circuitRepository: CircuitRepository,
        private val connectivityManager: NetworkConnectivityManager
) : BaseViewModel(), CircuitInfoViewModelInputs, CircuitInfoViewModelOutputs {

    private var circuitLat: Double? = null
    private var circuitLng: Double? = null
    private var wikipedia: String? = null
    private var name: String? = null

    private val circuitIdentifier: ConflatedBroadcastChannel<String> = ConflatedBroadcastChannel()

    private val circuit: Flow<Circuit?> = circuitIdentifier
        .asFlow()
        .flatMapLatest { circuitRepository.getCircuit(it) }

    override val goToMap: MutableLiveData<DataEvent<Pair<String, String>>> = MutableLiveData()
    override val goToWikipediaPage: MutableLiveData<DataEvent<String>> = MutableLiveData()

    override val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    override val circuitName: LiveData<String> = circuit
        .map { it?.name }
        .filterNotNull()
        .asLiveData(viewModelScope.coroutineContext)

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
                    list.add(CircuitItem.ErrorItem(SyncDataItem.ProvidedBy()))
                    return@map list
                }
            }
        }
        .then {
            isLoading.postValue(false)
        }
        .asLiveData(viewModelScope.coroutineContext)

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
        val mapsLatLng = "$circuitLat,$circuitLng"
        goToMap.postValue(DataEvent(Pair(mapsIntent, mapsLatLng)))
    }

    override fun clickWikipedia() {
        goToWikipediaPage.postValue(DataEvent(wikipedia ?: ""))
    }

    //endregion
}
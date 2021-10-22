package tmg.flashback.statistics.ui.circuit

import androidx.lifecycle.*
import java.lang.NullPointerException
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.data.db.stats.CircuitRepository
import tmg.flashback.data.models.stats.Circuit
import tmg.flashback.data.models.stats.Location
import tmg.flashback.statistics.extensions.circuitIcon
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
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
    private val connectivityManager: NetworkConnectivityManager,
    private val crashController: CrashController
) : ViewModel(), CircuitInfoViewModelInputs, CircuitInfoViewModelOutputs {

    private var circuitLocation: Location? = null
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
            circuitLocation = it?.location
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
        circuitLocation?.let { location ->
            val mapsIntent = "geo:0,0?q=${location.lat},${location.lng} ($name)"
            val mapsLatLng = "${location.lat},${location.lng}"
            goToMap.postValue(DataEvent(Pair(mapsIntent, mapsLatLng)))
        } ?: crashController.logException(NullPointerException("Circuit location is null for ${circuitIdentifier.valueOrNull}"))
    }

    override fun clickWikipedia() {
        goToWikipediaPage.postValue(DataEvent(wikipedia ?: ""))
    }

    //endregion
}
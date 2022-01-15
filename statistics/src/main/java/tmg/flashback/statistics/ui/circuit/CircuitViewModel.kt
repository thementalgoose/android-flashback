package tmg.flashback.statistics.ui.circuit

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.model.CircuitHistory
import tmg.flashback.formula1.model.Location
import tmg.flashback.statistics.extensions.circuitIcon
import tmg.flashback.statistics.repo.CircuitRepository
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable
import tmg.utilities.lifecycle.DataEvent

//region Inputs

interface CircuitViewModelInputs {
    fun circuitId(circuitId: String)
    fun clickShowOnMap(location: Location, name: String)
    fun clickLink(link: String)

    fun refresh()
}

//endregion

//region Outputs

interface CircuitViewModelOutputs {
    val list: LiveData<List<CircuitItem>>
    val circuitName: LiveData<String>

    val showLoading: LiveData<Boolean>

    val goToMap: LiveData<DataEvent<Pair<String, String>>> // Maps URI, lat / lng
    val goToLink: MutableLiveData<DataEvent<String>>
}

//endregion

@Suppress("EXPERIMENTAL_API_USAGE")
class CircuitViewModel(
    private val circuitRepository: CircuitRepository,
    private val networkConnectivityManager: NetworkConnectivityManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel(), CircuitViewModelInputs, CircuitViewModelOutputs {

    private val circuitId: MutableStateFlow<String?> = MutableStateFlow(null)
    private val circuitIdWithRequest: Flow<String?> = circuitId
        .filterNotNull()
        .flatMapLatest { id ->
            return@flatMapLatest flow {
                if (circuitRepository.getCircuitRounds(id) == 0) {
                    showLoading.postValue(true)
                    emit(null)
                    circuitRepository.fetchCircuit(id)
                    showLoading.postValue(false)
                    emit(id)
                }
                else {
                    emit(id)
                }
            }
        }
        .flowOn(ioDispatcher)

    private val isConnected: Boolean
        get() = networkConnectivityManager.isConnected

    override val goToMap: MutableLiveData<DataEvent<Pair<String, String>>> = MutableLiveData()
    override val goToLink: MutableLiveData<DataEvent<String>> = MutableLiveData()
    override val showLoading: MutableLiveData<Boolean> = MutableLiveData()
    override val circuitName: MutableLiveData<String> = MutableLiveData()

    override val list: LiveData<List<CircuitItem>> = circuitIdWithRequest
        .flatMapLatest { id ->
            if (id == null) {
                return@flatMapLatest flow {
                    emit(mutableListOf<CircuitItem>(CircuitItem.ErrorItem(SyncDataItem.Skeleton)))
                }
            }

            return@flatMapLatest circuitRepository.getCircuitHistory(id)
                .map {
                    val list = it.getHeaderList()
                    if (it != null) {
                        circuitName.postValue(it.data.name)
                    }
                    when {
                        (it == null || it.results.isEmpty()) && !isConnected -> list.addError(SyncDataItem.PullRefresh)
                        (it == null || it.results.isEmpty()) -> list.addError(SyncDataItem.Unavailable(DataUnavailable.CONSTRUCTOR_HISTORY_INTERNAL_ERROR))
                        else -> {
                            list.addAll(it.results
                                .map { result ->
                                    CircuitItem.Race(
                                        name = result.name,
                                        season = result.season,
                                        round = result.round,
                                        date = result.date,
                                        time = result.time,
                                        circuitName = it.data.name,
                                        country = it.data.country,
                                        countryISO = it.data.countryISO
                                    )
                                }
                                .sortedByDescending { it.season * 1000 + it.round }
                            )
                        }
                    }
                    return@map list
                }
        }
        .asLiveData(viewModelScope.coroutineContext)

    var inputs: CircuitViewModelInputs = this
    var outputs: CircuitViewModelOutputs = this

    //region Inputs

    override fun circuitId(circuitId: String) {
        this.circuitId.value = circuitId
    }

    override fun clickShowOnMap(location: Location, name: String) {
        val mapsIntent = "geo:0,0?q=${location.lat},${location.lng} ($name)"
        val mapsLatLng = "${location.lat},${location.lng}"
        goToMap.postValue(DataEvent(Pair(mapsIntent, mapsLatLng)))
    }

    override fun clickLink(link: String) {
        goToLink.postValue(DataEvent(link))
    }

    override fun refresh() {
        this.refresh(circuitId.value)
    }
    private fun refresh(circuitId: String? = this.circuitId.value) {
        viewModelScope.launch(context = ioDispatcher) {
            circuitId?.let {
                circuitRepository.fetchCircuit(circuitId)
                showLoading.postValue(false)
            }
        }
    }

    //endregion

    private fun CircuitHistory?.getHeaderList(): MutableList<CircuitItem> {
        val list = mutableListOf<CircuitItem>()
        if (this == null) {
            return list
        }
        this.data.circuitIcon?.let {
            list.add(CircuitItem.TrackImage(it))
        }
        list.add(CircuitItem.CircuitInfo(this))
        return list
    }
}
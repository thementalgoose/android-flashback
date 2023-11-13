package tmg.flashback.circuits.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import tmg.flashback.circuits.contract.model.ScreenCircuitData
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.device.usecases.OpenLocationUseCase
import tmg.flashback.domain.repo.CircuitRepository
import tmg.flashback.formula1.model.CircuitHistory
import tmg.flashback.web.usecases.OpenWebpageUseCase
import javax.inject.Inject

interface CircuitViewModelInputs {
    fun linkClicked(link: String)
    fun locationClicked(lat: Double, lng: Double, name: String)
    fun itemClicked(model: CircuitModel.Item)
    fun refresh()
    fun back()
}

interface CircuitViewModelOutputs {
    val uiState: StateFlow<CircuitScreenState>
}

@HiltViewModel
class CircuitViewModel @Inject constructor(
    private val circuitRepository: CircuitRepository,
    private val openWebpageUseCase: OpenWebpageUseCase,
    private val openLocationUseCase: OpenLocationUseCase,
    private val networkConnectivityManager: NetworkConnectivityManager,
    savedStateHandle: SavedStateHandle,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), CircuitViewModelInputs, CircuitViewModelOutputs {

    val inputs: CircuitViewModelInputs = this
    val outputs: CircuitViewModelOutputs = this

    override val uiState: MutableStateFlow<CircuitScreenState>

    init {
        val circuit: ScreenCircuitData = savedStateHandle.get<ScreenCircuitData>("data")!!

        uiState = MutableStateFlow(
            CircuitScreenState(
                circuitId = circuit.circuitId,
                circuitName = circuit.circuitName
            )
        )
        refresh()
    }

    override fun refresh() {
        viewModelScope.launch(ioDispatcher) {
            val circuitId = uiState.value.circuitId
            populate(circuitId)
            uiState.value = uiState.value.copy(isLoading = true, networkAvailable = networkConnectivityManager.isConnected)
            circuitRepository.fetchCircuit(circuitId)
            populate(circuitId)
        }
    }

    private suspend fun populate(circuitId: String) {
        val circuitHistory = circuitRepository.getCircuitHistory(circuitId).firstOrNull()
        val list = circuitHistory?.generateResultList()
        val statsList: List<CircuitModel> = circuitHistory?.toStatModel()?.let { listOf(it) } ?: emptyList()
        uiState.value = uiState.value.copy(
            circuit = circuitHistory?.data,
            isLoading = false,
            networkAvailable = networkConnectivityManager.isConnected,
            list = statsList + (list ?: emptyList())
        )
    }

    private fun CircuitHistory.generateResultList(): List<CircuitModel> = this
        .results
        .map { result ->
            CircuitModel.Item(
                circuitId = this.data.id,
                circuitName = this.data.name,
                country = this.data.country,
                countryISO = this.data.countryISO,
                result
            )
        }
        .sortedByDescending { it.data.season * 1000 + it.data.round }


    override fun linkClicked(link: String) {
        openWebpageUseCase.open(url = link, title = "")
    }

    override fun locationClicked(lat: Double, lng: Double, name: String) {
        openLocationUseCase.openLocation(lat, lng, name)
    }

    override fun itemClicked(model: CircuitModel.Item) {
        uiState.value = uiState.value.copy(
            selectedRace = model.data
        )
    }

    override fun back() {
        uiState.value = uiState.value.copy(
            selectedRace = null
        )
    }

    private fun CircuitHistory.toStatModel(): CircuitModel.Stats {
        return CircuitModel.Stats(
            circuitId = this.data.id,
            name = this.data.name,
            country = this.data.country,
            countryISO = this.data.countryISO,
            numberOfGrandPrix = this.results.size,
            startYear = this.results.minByOrNull { it.season }?.season,
            endYear = this.results.maxByOrNull { it.season }?.season,
            wikipedia = this.data.wikiUrl,
            location = this.data.location,
        )
    }
}
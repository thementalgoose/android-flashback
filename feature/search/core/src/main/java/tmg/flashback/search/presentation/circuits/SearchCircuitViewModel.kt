package tmg.flashback.search.presentation.circuits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import tmg.flashback.circuits.contract.Circuit
import tmg.flashback.circuits.contract.with
import tmg.flashback.data.repo.CircuitRepository
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import javax.inject.Inject

interface SearchCircuitViewModelInputs {
    fun searchTerm(input: String)
    fun clickCircuit(circuit: Circuit)
    fun refresh()
}

interface SearchCircuitViewModelOutputs {
    val uiState: StateFlow<SearchCircuitScreenState>
}

@HiltViewModel
class SearchCircuitViewModel @Inject constructor(
    private val circuitRepository: CircuitRepository,
    private val navigator: Navigator
): ViewModel(), SearchCircuitViewModelInputs, SearchCircuitViewModelOutputs {

    override val uiState: MutableStateFlow<SearchCircuitScreenState> =
        MutableStateFlow(SearchCircuitScreenState())

    init {
        viewModelScope.launch {
            val all = circuitRepository.getCircuits().firstOrNull().sortByName()
            uiState.value = uiState.value.copy(
                all = all,
                filtered = all.filtered(uiState.value.searchTerm),
                isLoading = false,
            )
        }
    }

    override fun searchTerm(input: String) {
        uiState.value = uiState.value.copy(
            searchTerm = input,
            filtered = uiState.value.all.filtered(input)
        )
    }

    override fun refresh() {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(isLoading = true)
            circuitRepository.fetchCircuits()
            val all = circuitRepository.getCircuits().firstOrNull().sortByName()
            uiState.value = uiState.value.copy(
                all = all,
                filtered = all.filtered(uiState.value.searchTerm),
                isLoading = false
            )
        }
    }

    override fun clickCircuit(circuit: Circuit) {
        navigator.navigate(Screen.Circuit.with(circuitId = circuit.id, circuitName = circuit.name))
    }

    private fun List<Circuit>.filtered(searchTerm: String): List<Circuit> {
        if (searchTerm.isEmpty()) {
            return this
        }
        return this.filter { it.searchTerm.split(" ").any { term -> term.contains(searchTerm.lowercase()) } }
    }
    private fun List<Circuit>?.sortByName(): List<Circuit> {
        return this?.sortedBy { it.name.lowercase() } ?: emptyList()
    }
    private val Circuit.searchTerm: String
        get() = "${this.name} ${this.city} ${this.country}".lowercase()
}
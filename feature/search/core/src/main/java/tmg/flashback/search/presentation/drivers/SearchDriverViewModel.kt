package tmg.flashback.search.presentation.drivers

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import tmg.flashback.data.repo.DriverRepository
import tmg.flashback.drivers.contract.Driver
import tmg.flashback.drivers.contract.with
import tmg.flashback.formula1.model.Driver
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import javax.inject.Inject

interface SearchDriverViewModelInputs {
    fun searchTerm(input: String)
    fun clickDriver(driver: Driver)
    fun refresh()
}

interface SearchDriverViewModelOutputs {
    val uiState: StateFlow<SearchDriverScreenState>
}

@HiltViewModel
class SearchDriverViewModel @Inject constructor(
    private val driverRepository: DriverRepository,
    private val navigator: Navigator,
): ViewModel(), SearchDriverViewModelInputs, SearchDriverViewModelOutputs {

    override val uiState: MutableStateFlow<SearchDriverScreenState> =
        MutableStateFlow(SearchDriverScreenState())

    init {
        viewModelScope.launch {
            val all = driverRepository.getDrivers().firstOrNull().sortByName()
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
            driverRepository.fetchDrivers()
            val all = driverRepository.getDrivers().firstOrNull().sortByName()
            uiState.value = uiState.value.copy(
                all = all,
                filtered = all.filtered(uiState.value.searchTerm),
                isLoading = false
            )
        }
    }

    override fun clickDriver(driver: Driver) {
        navigator.navigate(Screen.Driver.with(driverId = driver.id, driverName = driver.name))
    }

    private fun List<Driver>.filtered(searchTerm: String): List<Driver> {
        if (searchTerm.isEmpty()) {
            return this
        }
        return this.filter { it.searchTerm.split(" ").any { term -> term.contains(searchTerm.lowercase()) } }
    }
    private fun List<Driver>?.sortByName(): List<Driver> {
        return this?.sortedBy { it.lastName.lowercase() } ?: emptyList()
    }
    private val Driver.searchTerm: String
        get() = "${this.name} ${this.nationality} ${this.number} ${this.code}".lowercase()
}
package tmg.flashback.search.presentation.constructors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import tmg.flashback.constructors.contract.Constructor
import tmg.flashback.constructors.contract.with
import tmg.flashback.data.repo.ConstructorRepository
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import javax.inject.Inject


interface SearchConstructorViewModelInputs {
    fun searchTerm(input: String)
    fun clickConstructor(constructor: Constructor)
    fun refresh()
}

interface SearchConstructorViewModelOutputs {
    val uiState: StateFlow<SearchConstructorScreenState>
}

@HiltViewModel
class SearchConstructorViewModel @Inject constructor(
    private val constructorRepository: ConstructorRepository,
    private val navigator: Navigator
): ViewModel(), SearchConstructorViewModelInputs, SearchConstructorViewModelOutputs {

    override val uiState: MutableStateFlow<SearchConstructorScreenState> =
        MutableStateFlow(SearchConstructorScreenState())

    init {
        viewModelScope.launch {
            val all = constructorRepository.getConstructors().firstOrNull().sortByName()
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
            constructorRepository.fetchConstructors()
            val all = constructorRepository.getConstructors().firstOrNull().sortByName()
            uiState.value = uiState.value.copy(
                all = all,
                filtered = all.filtered(uiState.value.searchTerm),
                isLoading = false
            )
        }
    }

    override fun clickConstructor(constructor: Constructor) {
        navigator.navigate(Screen.Constructor.with(constructorId = constructor.id, constructorName = constructor.name))
    }

    private fun List<Constructor>.filtered(searchTerm: String): List<Constructor> {
        if (searchTerm.isEmpty()) {
            return this
        }
        return this.filter {
            it.searchTerm.contains(searchTerm.lowercase())
        }
    }
    private fun List<Constructor>?.sortByName(): List<Constructor> {
        return this?.sortedBy { it.name.lowercase() } ?: emptyList()
    }
    private val Constructor.searchTerm: String
        get() = "${this.name} ${this.nationality}".lowercase()
}
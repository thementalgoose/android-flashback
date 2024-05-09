package tmg.flashback.search.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.circuits.contract.Circuit
import tmg.flashback.circuits.contract.with
import tmg.flashback.constructors.contract.Constructor
import tmg.flashback.constructors.contract.with
import tmg.flashback.domain.repo.CircuitRepository
import tmg.flashback.domain.repo.ConstructorRepository
import tmg.flashback.domain.repo.DriverRepository
import tmg.flashback.domain.repo.OverviewRepository
import tmg.flashback.drivers.contract.Driver
import tmg.flashback.drivers.contract.with
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import javax.inject.Inject

interface SearchViewModelInputs {
    fun search(input: String)
    fun searchClear()
    fun refresh()
    fun clickDriver(driver: Driver)
    fun clickConstructor(constructor: Constructor)
    fun clickCircuit(circuit: Circuit)
    fun clickRace(overviewRace: OverviewRace)
    fun back()
}

interface SearchViewModelOutputs {
    val uiState: StateFlow<SearchScreenState>
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val driverRepository: DriverRepository,
    private val constructorRepository: ConstructorRepository,
    private val circuitRepository: CircuitRepository,
    private val overviewRepository: OverviewRepository,
    private val adsRepository: AdsRepository,
    private val navigator: Navigator,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), SearchViewModelInputs, SearchViewModelOutputs {

    val inputs: SearchViewModelInputs = this
    val outputs: SearchViewModelOutputs = this

    private val search: MutableStateFlow<String> = MutableStateFlow("")
    private val selected: MutableStateFlow<OverviewRace?> = MutableStateFlow(null)

    private val isAdsEnabled: Boolean by lazy {
        adsRepository.areAdvertsEnabled && adsRepository.advertConfig.onSearch
    }

    @Suppress("UNCHECKED_CAST")
    override val uiState: StateFlow<SearchScreenState> = combine(
        driverRepository.getDrivers()
            .map { it
                .map { item -> item to item.searchTerm }
                .sortedBy { it.first.lastName.lowercase() }
             },
        constructorRepository.getConstructors()
            .map { it
                .map { item -> item to item.searchTerm }
                .sortedBy { it.first.name.lowercase() }
             },
        circuitRepository.getCircuits()
            .map { it
                .map { item -> item to item.searchTerm }
                .sortedBy { it.first.name.lowercase() }
             },
        overviewRepository.getOverview()
            .map { item -> item
                .sortedByDescending { it.round }
                .sortedByDescending { it.season }
                .map { it to it.searchTerm }
             },
        search,
        selected,
    ) { flows ->
        val searchTerm = (flows[4] as String)
        val searchTermLowercase = searchTerm.lowercase()
        SearchScreenState(
            drivers = (flows[0] as List<Pair<Driver, String>>)
                .filter { it.second.containsWithSpaces(searchTermLowercase) }
                .map { it.first },
            constructors = (flows[1] as List<Pair<Constructor, String>>)
                .filter { it.second.containsWithSpaces(searchTermLowercase) }
                .map { it.first },
            circuits = (flows[2] as List<Pair<Circuit, String>>)
                .filter { it.second.containsWithSpaces(searchTermLowercase) }
                .map { it.first },
            races = (flows[3] as List<Pair<OverviewRace, String>>)
                .filter { it.second.containsWithSpaces(searchTermLowercase) }
                .map { it.first },
            searchTerm = searchTerm,
            showAdvert = isAdsEnabled,
            selected = flows[5] as? OverviewRace?
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, SearchScreenState())

    override fun search(input: String) {
        this.search.value = input
    }

    override fun searchClear() {
        this.search.value = ""
    }

    override fun refresh() {
        viewModelScope.launch(ioDispatcher) {
            val result = awaitAll(
                async { driverRepository.fetchDrivers() },
                async { constructorRepository.fetchConstructors() },
                async { overviewRepository.fetchOverview() },
                async { circuitRepository.fetchCircuits() }
            ).reduce { a, b -> a && b }
            Log.i("Search", "Search updated $result")
        }
    }

    private fun String.containsWithSpaces(searchTerm: String): Boolean {
        return searchTerm.split(" ")
            .all { this.contains(it) }
    }

    override fun clickCircuit(circuit: Circuit) {
        navigator.navigate(Screen.Circuit.with(
            circuitId = circuit.id,
            circuitName = circuit.name
        ))
    }

    override fun clickDriver(driver: Driver) {
        navigator.navigate(Screen.Driver.with(
            driverId = driver.id,
            driverName = driver.name
        ))
    }

    override fun clickConstructor(constructor: Constructor,) {
        navigator.navigate(Screen.Constructor.with(
            constructorId = constructor.id,
            constructorName = constructor.name
        ))
    }

    override fun clickRace(overviewRace: OverviewRace) {
        selected.value = overviewRace
    }

    override fun back() {
        selected.value = null
    }

    private val Driver.searchTerm: String
        get() = "${name.lowercase()} ${nationality.lowercase()}"
    private val Constructor.searchTerm: String
        get() = "${name.lowercase()} ${nationality.lowercase()}"
    private val Circuit.searchTerm: String
        get() = "${name.lowercase()} ${city.lowercase()} ${country.lowercase()}"
    private val OverviewRace.searchTerm: String
        get() = "$season $round $season ${raceName.lowercase()} ${circuitName.lowercase()} ${country.lowercase()} ${DateTimeFormatter.ofPattern("MMMM").format(date).lowercase()}"
}
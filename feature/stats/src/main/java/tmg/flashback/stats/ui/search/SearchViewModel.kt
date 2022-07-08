package tmg.flashback.stats.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tmg.flashback.ads.repository.AdsRepository
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.statistics.repo.CircuitRepository
import tmg.flashback.statistics.repo.ConstructorRepository
import tmg.flashback.statistics.repo.DriverRepository
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.ui.weekend.WeekendInfo
import tmg.utilities.extensions.extend

interface SearchViewModelInputs {
    fun inputSearch(search: String)
    fun inputCategory(searchCategory: SearchCategory)
    fun clickItem(item: SearchItem)
    fun refresh()
}

interface SearchViewModelOutputs {
    val results: LiveData<List<SearchItem>>
    val selectedCategory: LiveData<SearchCategory?>
    val isLoading: LiveData<Boolean>
}

class SearchViewModel(
    private val driverRepository: DriverRepository,
    private val constructorRepository: ConstructorRepository,
    private val circuitRepository: CircuitRepository,
    private val overviewRepository: OverviewRepository,
    private val adsRepository: AdsRepository,
    private val statsNavigationComponent: StatsNavigationComponent,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), SearchViewModelInputs, SearchViewModelOutputs {

    val inputs: SearchViewModelInputs = this
    val outputs: SearchViewModelOutputs = this

    private val category: MutableStateFlow<SearchCategory?> = MutableStateFlow(null)
    private val search: MutableStateFlow<String> = MutableStateFlow("")
    override val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    override val results: LiveData<List<SearchItem>> = category
        .filterNotNull()
        .flatMapLatest { category ->
            when (category) {
                SearchCategory.DRIVER -> driverRepository
                    .getDrivers()
                    .map { it.sortedBy { "${it.firstName} ${it.lastName}" }}
                    .mapDrivers()
                SearchCategory.CONSTRUCTOR -> constructorRepository
                    .getConstructors()
                    .map { it.sortedBy { it.name }}
                    .mapConstructors()
                SearchCategory.CIRCUIT -> circuitRepository
                    .getCircuits()
                    .map { it.sortedBy { it.name }}
                    .mapCircuits()
                SearchCategory.RACE -> overviewRepository
                    .getOverview()
                    .map {
                        it.sortedByDescending { "${it.season}-${it.round.extend(2)}" }
                    }
                    .mapRaces()
            }
        }
        .combine(search) { searchItems, searchTerm ->
            searchItems.filter {
                if (it.searchBy == null) {
                    return@filter true
                }
                if (searchTerm.isBlank() || searchTerm.isEmpty()) {
                    return@filter true
                }
                return@filter it.searchBy.contains(searchTerm.lowercase())
            }
        }
        .map { list ->
            if (list.none { it !is SearchItem.Advert }) {
                return@map listOf(SearchItem.ErrorItem)
            }
            else {
                return@map list
            }
        }
        .onStart {
            emit(mutableListOf<SearchItem>().apply {
                if (adsRepository.advertConfig.onSearch) {
                    add(SearchItem.Advert)
                }
                add(SearchItem.Placeholder)
            })
        }
        .flowOn(ioDispatcher)
        .asLiveData(viewModelScope.coroutineContext)

    override val selectedCategory: LiveData<SearchCategory?> = category
        .asLiveData(viewModelScope.coroutineContext)

    override fun inputCategory(searchCategory: SearchCategory) {
        this.category.value = searchCategory
    }

    override fun inputSearch(search: String) {
        this.search.value = search
    }

    override fun clickItem(item: SearchItem) {
        when (item) {
            is SearchItem.Circuit -> {
                statsNavigationComponent.circuit(
                    circuitId = item.circuitId,
                    circuitName = item.name
                )
            }
            is SearchItem.Constructor -> {
                statsNavigationComponent.constructorOverview(
                    id = item.constructorId,
                    name = item.name
                )
            }
            is SearchItem.Driver -> {
                statsNavigationComponent.driverOverview(
                    id = item.driverId,
                    name = item.name
                )
            }
            is SearchItem.Race -> {
                statsNavigationComponent.weekend(
                    weekendInfo = WeekendInfo(
                        season = item.season,
                        round = item.round,
                        raceName = item.raceName,
                        circuitId = item.circuitId,
                        circuitName = item.circuitName,
                        country = item.country,
                        countryISO = item.countryISO,
                        date = item.date
                    )
                )
            }
            SearchItem.Advert -> {}
            SearchItem.ErrorItem -> {}
            SearchItem.Placeholder -> {}
        }
    }

    override fun refresh() {
        viewModelScope.launch(ioDispatcher) {
            isLoading.postValue(true)
            when (category.value) {
                SearchCategory.DRIVER -> {
                    driverRepository.fetchDrivers()
                }
                SearchCategory.CONSTRUCTOR -> {
                    constructorRepository.fetchConstructors()
                }
                SearchCategory.CIRCUIT -> {
                    circuitRepository.fetchCircuits()
                }
                SearchCategory.RACE -> {
                    overviewRepository.fetchOverview()
                }
                null -> { }
            }
            isLoading.postValue(false)
        }
    }

    private fun Flow<List<Driver>>.mapDrivers(): Flow<List<SearchItem>> {
        return this.map {
            mutableListOf<SearchItem>().apply {
                if (adsRepository.advertConfig.onSearch) {
                    add(SearchItem.Advert)
                }
                addAll(it.map {
                    SearchItem.Driver(
                        driverId = it.id,
                        name = "${it.firstName} ${it.lastName}",
                        nationality = it.nationality,
                        nationalityISO = it.nationalityISO,
                        imageUrl = it.photoUrl
                    )
                })
            }
        }
    }

    private fun Flow<List<Constructor>>.mapConstructors(): Flow<List<SearchItem>> {
        return this.map {
            mutableListOf<SearchItem>().apply {
                if (adsRepository.advertConfig.onSearch) {
                    add(SearchItem.Advert)
                }
                addAll(it.map {
                    SearchItem.Constructor(
                        constructorId = it.id,
                        name = it.name,
                        nationality = it.nationality,
                        nationalityISO = it.nationalityISO,
                        colour = it.color
                    )
                })
            }
        }
    }

    private fun Flow<List<Circuit>>.mapCircuits(): Flow<List<SearchItem>> {
        return this.map {
            mutableListOf<SearchItem>().apply {
                if (adsRepository.advertConfig.onSearch) {
                    add(SearchItem.Advert)
                }
                addAll(it.map {
                    SearchItem.Circuit(
                        circuitId = it.id,
                        name = it.name,
                        nationality = it.country,
                        nationalityISO = it.countryISO,
                        location = it.city
                    )
                })
            }
        }
    }

    private fun Flow<List<OverviewRace>>.mapRaces(): Flow<List<SearchItem>> {
        return this.map {
            mutableListOf<SearchItem>().apply {
                if (adsRepository.advertConfig.onSearch) {
                    add(SearchItem.Advert)
                }
                addAll(it.map {
                    SearchItem.Race(
                        raceId = "${it.season}-${it.round}",
                        season = it.season,
                        round = it.round,
                        raceName = it.raceName,
                        country = it.country,
                        circuitId = it.circuitId,
                        countryISO = it.countryISO,
                        circuitName = it.circuitName,
                        date = it.date
                    )
                })
            }
        }
    }
}
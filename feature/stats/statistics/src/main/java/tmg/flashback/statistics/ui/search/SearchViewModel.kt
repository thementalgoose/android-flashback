package tmg.flashback.statistics.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tmg.flashback.formula1.model.*
import tmg.flashback.statistics.repo.CircuitRepository
import tmg.flashback.statistics.repo.ConstructorRepository
import tmg.flashback.statistics.repo.DriverRepository
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable
import tmg.utilities.extensions.extend
import tmg.utilities.lifecycle.DataEvent

//region Inputs

interface SearchViewModelInputs {

    fun openCategory()

    fun inputSearch(search: String)
    fun inputCategory(searchCategory: SearchCategory)

    fun refresh()

    fun clickItem(searchItem: SearchItem)
}

//endregion

//region Outputs

interface SearchViewModelOutputs {

    val openCategoryPicker: LiveData<DataEvent<SearchCategory?>>
    val selectedCategory: LiveData<SearchCategory?>

    val openLink: LiveData<DataEvent<SearchItem>>
    val isLoading: LiveData<Boolean>

    val results: LiveData<List<SearchItem>>
}

//endregion

class SearchViewModel(
    private val driverRepository: DriverRepository,
    private val constructorRepository: ConstructorRepository,
    private val circuitRepository: CircuitRepository,
    private val overviewRepository: OverviewRepository
): ViewModel(), SearchViewModelInputs, SearchViewModelOutputs {

    var inputs: SearchViewModelInputs = this
    var outputs: SearchViewModelOutputs = this

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
            if (list.isEmpty()) {
                return@map listOf(SearchItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.NO_SEARCH_RESULTS)))
            }
            else {
                return@map list
            }
        }
        .onStart {
            emit(listOf(SearchItem.Placeholder))
        }
        .also {
            isLoading.value = false
        }
        .asLiveData(viewModelScope.coroutineContext)

    override val selectedCategory: LiveData<SearchCategory?> = category
        .asLiveData(viewModelScope.coroutineContext)

    override val openCategoryPicker: MutableLiveData<DataEvent<SearchCategory?>> = MutableLiveData()
    override val openLink: MutableLiveData<DataEvent<SearchItem>> = MutableLiveData()

    //region Inputs

    override fun inputCategory(searchCategory: SearchCategory) {
        if (category.value != searchCategory) {
            isLoading.value = true
            category.value = searchCategory
        }
    }

    override fun inputSearch(search: String) {
        this.search.value = search
    }

    override fun openCategory() {
        openCategoryPicker.value = DataEvent(category.value)
    }

    override fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            when (category.value) {
                SearchCategory.DRIVER -> {
                    val result = driverRepository.fetchDrivers()
                }
                SearchCategory.CONSTRUCTOR -> {
                    val result = constructorRepository.fetchConstructors()
                }
                SearchCategory.CIRCUIT -> {
                    val result = circuitRepository.fetchCircuits()
                }
                SearchCategory.RACE -> {
                    val result = overviewRepository.fetchOverview()
                }
            }
            isLoading.postValue(false)
        }
    }

    override fun clickItem(searchItem: SearchItem) {
        openLink.value = DataEvent(searchItem)
    }

    //endregion

    private fun Flow<List<Driver>>.mapDrivers(): Flow<List<SearchItem>> {
        return this.mapListItem {
            SearchItem.Driver(
                driverId = it.id,
                name = "${it.firstName} ${it.lastName}",
                nationality = it.nationality,
                nationalityISO = it.nationalityISO,
                imageUrl = it.photoUrl
            )
        }
    }


    private fun Flow<List<Constructor>>.mapConstructors(): Flow<List<SearchItem>> {
        return this.mapListItem {
            SearchItem.Constructor(
                constructorId = it.id,
                name = it.name,
                nationality = it.nationality,
                nationalityISO = it.nationalityISO,
                colour = it.color
            )
        }
    }


    private fun Flow<List<Circuit>>.mapCircuits(): Flow<List<SearchItem>> {
        return this.mapListItem {
            SearchItem.Circuit(
                circuitId = it.id,
                name = it.name,
                nationality = it.country,
                nationalityISO = it.countryISO,
                location = it.city
            )
        }
    }


    private fun Flow<List<OverviewRace>>.mapRaces(): Flow<List<SearchItem>> {
        return this
            .mapListItem {
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
            }
    }

    // TODO: Move this to utilities!
    private fun <T,R> Flow<List<T>>.mapListItem(callback: (item: T) -> R): Flow<List<R>> {
        return this.map { list ->
            list.map(callback)
        }
    }
}

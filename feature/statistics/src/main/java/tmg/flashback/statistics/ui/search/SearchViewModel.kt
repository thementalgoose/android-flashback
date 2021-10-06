package tmg.flashback.statistics.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.startWith
import tmg.flashback.data.db.stats.SearchRepository
import tmg.flashback.data.models.stats.History
import tmg.flashback.data.models.stats.HistoryRound
import tmg.flashback.data.models.stats.SearchCircuit
import tmg.flashback.data.models.stats.SearchConstructor
import tmg.flashback.data.models.stats.SearchDriver
import tmg.flashback.data.utils.extendTo
import tmg.flashback.statistics.ui.search.viewholder.SearchDriverViewHolder
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

//region Inputs

interface SearchViewModelInputs {

    fun openCategory()

    fun inputSearch(search: String)
    fun inputCategory(searchCategory: SearchCategory)

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
    private val searchRepository: SearchRepository
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
                SearchCategory.DRIVER -> searchRepository
                    .allDrivers()
                    .map { it.sortedBy { "${it.firstName} ${it.lastName}" }}
                    .mapDrivers()
                SearchCategory.CONSTRUCTOR -> searchRepository
                    .allConstructors()
                    .map { it.sortedBy { it.name }}
                    .mapConstructors()
                SearchCategory.CIRCUIT -> searchRepository
                    .allCircuits()
                    .map { it.sortedBy { it.name }}
                    .mapCircuits()
                SearchCategory.RACE -> searchRepository
                    .allRaces()
                    .mapListItem { it.rounds }
                    .map { it.flatten().sortedByDescending { "${it.season}-${it.round.extendTo(2)}" } }
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

    override fun clickItem(searchItem: SearchItem) {
        openLink.value = DataEvent(searchItem)
    }

    //endregion

    private fun Flow<List<SearchDriver>>.mapDrivers(): Flow<List<SearchItem>> {
        return this.mapListItem {
            SearchItem.Driver(
                driverId = it.id,
                name = "${it.firstName} ${it.lastName}",
                nationality = it.nationality,
                nationalityISO = it.nationalityISO,
                imageUrl = it.image
            )
        }
    }


    private fun Flow<List<SearchConstructor>>.mapConstructors(): Flow<List<SearchItem>> {
        return this.mapListItem {
            SearchItem.Constructor(
                constructorId = it.id,
                name = it.name,
                nationality = it.nationality,
                nationalityISO = it.nationalityISO,
                colour = it.colour
            )
        }
    }


    private fun Flow<List<SearchCircuit>>.mapCircuits(): Flow<List<SearchItem>> {
        return this.mapListItem {
            SearchItem.Circuit(
                circuitId = it.id,
                name = it.name,
                nationality = it.country,
                nationalityISO = it.countryISO,
                location = it.locationName
            )
        }
    }


    private fun Flow<List<HistoryRound>>.mapRaces(): Flow<List<SearchItem>> {
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

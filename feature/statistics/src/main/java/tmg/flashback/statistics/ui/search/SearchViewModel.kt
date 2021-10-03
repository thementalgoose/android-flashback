package tmg.flashback.statistics.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
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
import tmg.flashback.data.db.stats.SearchRepository
import tmg.flashback.data.models.stats.History
import tmg.flashback.data.models.stats.SearchCircuit
import tmg.flashback.data.models.stats.SearchConstructor
import tmg.flashback.data.models.stats.SearchDriver
import tmg.flashback.statistics.ui.search.viewholder.SearchDriverViewHolder
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

//region Inputs

interface SearchViewModelInputs {

    fun openCategory()

    fun inputSearch(search: String)
    fun inputCategory(searchCategory: SearchCategory)
}

//endregion

//region Outputs

interface SearchViewModelOutputs {

    val openCategoryPicker: LiveData<DataEvent<SearchCategory?>>
    val selectedCategory: LiveData<SearchCategory?>

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

    override val results: LiveData<List<SearchItem>> = category
        .filterNotNull()
        .flatMapLatest { category ->
            when (category) {
                SearchCategory.DRIVER -> searchRepository
                    .allDrivers()
                    .mapDrivers()
                SearchCategory.CONSTRUCTOR -> searchRepository
                    .allConstructors()
                    .mapConstructors()
                SearchCategory.CIRCUIT -> searchRepository
                    .allCircuits()
                    .mapCircuits()
                SearchCategory.RACE -> searchRepository
                    .allRaces()
                    .mapRaces()
            }
        }
        .combine(search) { searchItems, searchTerm ->
            println("Searching ${searchItems.size} items by $searchTerm")
            searchItems
        }
        .asLiveData(viewModelScope.coroutineContext)

    override val openCategoryPicker: MutableLiveData<DataEvent<SearchCategory?>> = MutableLiveData()
    override val selectedCategory: LiveData<SearchCategory?> = category
        .asLiveData(viewModelScope.coroutineContext)

    //region Inputs

    override fun inputCategory(searchCategory: SearchCategory) {
        category.value = searchCategory
    }

    override fun inputSearch(search: String) {
        this.search.value = search
    }

    override fun openCategory() {
        openCategoryPicker.value = DataEvent(category.value)
    }

    //endregion

    private fun Flow<List<SearchDriver>>.mapDrivers(): Flow<List<SearchItem>> {
        return this.mapListItem {
            SearchItem.Driver(
                driverId = it.id
            )
        }
    }


    private fun Flow<List<SearchConstructor>>.mapConstructors(): Flow<List<SearchItem>> {
        return this.mapListItem {
            SearchItem.Constructor(
                constructorId = it.id
            )
        }
    }


    private fun Flow<List<SearchCircuit>>.mapCircuits(): Flow<List<SearchItem>> {
        return this.mapListItem {
            SearchItem.Circuit(
                circuitId = it.id
            )
        }
    }


    private fun Flow<List<History>>.mapRaces(): Flow<List<SearchItem>> {
        return this
            .mapListItem { it.rounds }
            .map { it.flatten() }
            .mapListItem {
                SearchItem.Race(
                    raceId = "${it.season}-${it.round}"
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

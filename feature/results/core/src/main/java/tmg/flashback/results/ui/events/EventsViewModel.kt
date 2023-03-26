package tmg.flashback.results.ui.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import tmg.flashback.formula1.model.Event
import tmg.flashback.statistics.repo.EventsRepository
import javax.inject.Inject

interface EventsViewModelInputs {
    fun setup(season: Int)
}

interface EventsViewModelOutputs {
    val events: LiveData<List<Event>>
}

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventsRepository: EventsRepository,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel(), EventsViewModelInputs, EventsViewModelOutputs {

    val inputs: EventsViewModelInputs = this
    val outputs: EventsViewModelOutputs = this

    private val season: MutableStateFlow<Int?> = MutableStateFlow(null)
    override val events: LiveData<List<Event>> = season
        .filterNotNull()
        .flatMapLatest { eventsRepository.getEvents(it) }
        .map { list -> list.sortedBy { it.date } }
        .flowOn(ioDispatcher)
        .asLiveData(viewModelScope.coroutineContext)

    override fun setup(season: Int) {
        this.season.value = season
    }
}
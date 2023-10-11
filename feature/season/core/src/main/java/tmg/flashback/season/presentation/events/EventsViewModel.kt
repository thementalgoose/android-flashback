package tmg.flashback.season.presentation.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import tmg.flashback.domain.repo.EventsRepository
import tmg.flashback.formula1.model.Event
import javax.inject.Inject

interface EventsViewModelInputs {
    fun setup(season: Int)
}

interface EventsViewModelOutputs {
    val events: StateFlow<List<Event>>
}

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventsRepository: EventsRepository,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel(), EventsViewModelInputs, EventsViewModelOutputs {

    val inputs: EventsViewModelInputs = this
    val outputs: EventsViewModelOutputs = this

    private val season: MutableStateFlow<Int?> = MutableStateFlow(null)
    override val events: StateFlow<List<Event>> = season
        .filterNotNull()
        .flatMapLatest { eventsRepository.getEvents(it) }
        .map { list -> list.sortedBy { it.date } }
        .flowOn(ioDispatcher)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    override fun setup(season: Int) {
        this.season.value = season
    }
}
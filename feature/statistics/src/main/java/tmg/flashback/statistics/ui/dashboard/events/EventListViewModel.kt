package tmg.flashback.statistics.ui.dashboard.events

import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import tmg.flashback.formula1.enums.EventType
import tmg.flashback.formula1.model.Event
import tmg.flashback.statistics.repo.EventsRepository
import tmg.utilities.extensions.combinePair

//region Inputs

interface EventListViewModelInputs {
    fun show(season: Int, type: EventType)
}

//endregion

//region Outputs

interface EventListViewModelOutputs {
    val list: LiveData<List<Event>>
}

//endregion

class EventListViewModel(
    private val eventsRepository: EventsRepository
): ViewModel(), EventListViewModelInputs, EventListViewModelOutputs {

    val inputs: EventListViewModelInputs = this
    val outputs: EventListViewModelOutputs = this

    private val season: MutableStateFlow<Int> = MutableStateFlow(0)
    private val eventType: MutableStateFlow<EventType?> = MutableStateFlow(null)

    override val list: LiveData<List<Event>> = season
        .filter { it != 0 }
        .flatMapLatest { eventsRepository.getEvents(it) }
        .combinePair(eventType)
        .map { (events, eventType) ->
            events.filter { it.type == eventType }
        }
        .asLiveData(viewModelScope.coroutineContext)

    override fun show(season: Int, type: EventType) {
        this.season.value = season
        this.eventType.value = type
    }
}
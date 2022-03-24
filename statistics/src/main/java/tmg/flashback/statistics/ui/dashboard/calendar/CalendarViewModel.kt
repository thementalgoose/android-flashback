package tmg.flashback.statistics.ui.dashboard.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.statistics.repository.HomeRepository
import tmg.flashback.statistics.usecases.DefaultSeasonUseCase
import tmg.flashback.statistics.usecases.WeekendOverviewUseCase

interface CalendarViewModelInputs {

}

interface CalendarViewModelOutputs {
    val items: LiveData<List<OverviewRace>>
}

class CalendarViewModel(
    private val weekendOverviewUseCase: WeekendOverviewUseCase,
    private val defaultSeasonUseCase: DefaultSeasonUseCase
): ViewModel(), CalendarViewModelInputs, CalendarViewModelOutputs {

    val inputs: CalendarViewModelInputs = this
    val outputs: CalendarViewModelOutputs = this

    private val season: MutableStateFlow<Int> = MutableStateFlow(defaultSeasonUseCase.defaultSeason)

    override val items: LiveData<List<OverviewRace>> = season
        .flatMapLatest { weekendOverviewUseCase.get(it) }
        .filterNotNull()
        .asLiveData(viewModelScope.coroutineContext)
}
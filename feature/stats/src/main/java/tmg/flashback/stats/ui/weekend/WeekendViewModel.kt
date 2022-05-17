package tmg.flashback.stats.ui.weekend

import androidx.lifecycle.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import tmg.flashback.formula1.model.RaceInfo

interface WeekendViewModelInputs {
    fun load()
    fun clickTab(state: WeekendNavItem)
    fun refresh()
}

interface WeekendViewModelOutputs {
    val tabs: LiveData<List<WeekendScreenState>>
}

class WeekendViewModel(): ViewModel(), WeekendViewModelInputs, WeekendViewModelOutputs {

    val inputs: WeekendViewModelInputs = this
    val outputs: WeekendViewModelOutputs = this

    private val selectedTab: MutableStateFlow<WeekendNavItem> = MutableStateFlow(WeekendNavItem.SCHEDULE)

    override val tabs: LiveData<List<WeekendScreenState>> = selectedTab
        .map {
            listOf(
                WeekendScreenState(tab = WeekendNavItem.SCHEDULE, isSelected = it == WeekendNavItem.SCHEDULE),
                WeekendScreenState(tab = WeekendNavItem.QUALIFYING, isSelected = it == WeekendNavItem.QUALIFYING),
//                WeekendScreenState(tab = WeekendNavItem.SPRINT, isSelected = it == WeekendNavItem.SPRINT),
                WeekendScreenState(tab = WeekendNavItem.RACE, isSelected = it == WeekendNavItem.RACE),
                WeekendScreenState(tab = WeekendNavItem.CONSTRUCTOR, isSelected = it == WeekendNavItem.CONSTRUCTOR),
            )
        }
        .asLiveData(viewModelScope.coroutineContext)

    override fun clickTab(state: WeekendNavItem) {
        selectedTab.value = state
    }

    override fun load() {

    }

    override fun refresh() {

    }
}
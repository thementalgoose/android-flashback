package tmg.flashback.stats.ui.weekend

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.statistics.repo.RaceRepository

interface WeekendViewModelInputs {
    fun load(season: Int, round: Int)
    fun clickTab(state: WeekendNavItem)
    fun refresh()
}

interface WeekendViewModelOutputs {
    val tabs: LiveData<List<WeekendScreenState>>
    val isRefreshing: LiveData<Boolean>
}

class WeekendViewModel(
    private val raceRepository: RaceRepository,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel(), WeekendViewModelInputs, WeekendViewModelOutputs {

    val inputs: WeekendViewModelInputs = this
    val outputs: WeekendViewModelOutputs = this

    private val selectedTab: MutableStateFlow<WeekendNavItem> = MutableStateFlow(WeekendNavItem.SCHEDULE)
    private val seasonRound: MutableStateFlow<Pair<Int, Int>?> = MutableStateFlow(null)

    override val tabs: LiveData<List<WeekendScreenState>> =
        selectedTab
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

    override val isRefreshing: MutableLiveData<Boolean> = MutableLiveData()

    override fun clickTab(state: WeekendNavItem) {
        selectedTab.value = state
    }

    override fun load(season: Int, round: Int) {
        seasonRound.value = Pair(season, round)
    }

    override fun refresh() {
        seasonRound.value?.first?.let { season ->
            isRefreshing.postValue(true)
            viewModelScope.launch(ioDispatcher) {
                raceRepository.fetchRaces(season)
                isRefreshing.postValue(false)
            }
        }
    }
}
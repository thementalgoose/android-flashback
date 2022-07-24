package tmg.flashback.stats.ui.weekend

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.statistics.repo.RaceRepository
import tmg.utilities.extensions.combinePair

interface WeekendViewModelInputs {
    fun load(season: Int, round: Int)
    fun clickTab(state: WeekendNavItem)
    fun refresh()
}

interface WeekendViewModelOutputs {
    val tabs: LiveData<List<WeekendScreenState>>
    val isRefreshing: LiveData<Boolean>
    val weekendInfo: LiveData<WeekendInfo>
}

class WeekendViewModel(
    private val raceRepository: RaceRepository,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel(), WeekendViewModelInputs, WeekendViewModelOutputs {

    val inputs: WeekendViewModelInputs = this
    val outputs: WeekendViewModelOutputs = this

    private val selectedTab: MutableStateFlow<WeekendNavItem> = MutableStateFlow(WeekendNavItem.SCHEDULE)
    private val seasonRound: MutableStateFlow<Pair<Int, Int>?> = MutableStateFlow(null)

    private val raceFlow: Flow<Race?> = seasonRound
        .filterNotNull()
        .flatMapLatest { (season, round) -> raceRepository.getRace(season, round) }
        .shareIn(viewModelScope, started = SharingStarted.Eagerly)

    override val weekendInfo: LiveData<WeekendInfo> = raceFlow
        .filterNotNull()
        .map {
            it.raceInfo.toWeekendInfo()
        }
        .asLiveData(viewModelScope.coroutineContext)

    override val tabs: LiveData<List<WeekendScreenState>> = raceFlow
        .combinePair(selectedTab)
        .map { (race, navItem) ->
            val list = mutableListOf<WeekendScreenState>()
            list.add(WeekendScreenState(WeekendNavItem.SCHEDULE, isSelected = navItem == WeekendNavItem.SCHEDULE))
            list.add(WeekendScreenState(WeekendNavItem.QUALIFYING, isSelected = navItem == WeekendNavItem.QUALIFYING))
            if (race?.hasSprintQualifying == true) {
                list.add(WeekendScreenState(WeekendNavItem.SPRINT, isSelected = navItem == WeekendNavItem.SPRINT))
            }
            list.add(WeekendScreenState(WeekendNavItem.RACE, isSelected = navItem == WeekendNavItem.RACE))
            list.add(WeekendScreenState(WeekendNavItem.CONSTRUCTOR, isSelected = navItem == WeekendNavItem.CONSTRUCTOR))
            return@map list
        }
        .onStart { listOf(
            WeekendScreenState(WeekendNavItem.SCHEDULE, isSelected = selectedTab.value == WeekendNavItem.SCHEDULE),
            WeekendScreenState(WeekendNavItem.QUALIFYING, isSelected = selectedTab.value == WeekendNavItem.QUALIFYING),
            WeekendScreenState(WeekendNavItem.RACE, isSelected = selectedTab.value == WeekendNavItem.RACE),
            WeekendScreenState(WeekendNavItem.CONSTRUCTOR, isSelected = selectedTab.value == WeekendNavItem.CONSTRUCTOR)
        ) }
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
package tmg.flashback.stats.ui.weekend

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tmg.flashback.formula1.constants.Formula1.currentSeasonYear
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.stats.usecases.DefaultSeasonUseCase
import tmg.utilities.extensions.combinePair
import javax.inject.Inject

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

@HiltViewModel
class WeekendViewModel @Inject constructor(
    private val getDefaultSeasonUseCase: DefaultSeasonUseCase,
    private val raceRepository: RaceRepository,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel(), WeekendViewModelInputs, WeekendViewModelOutputs {

    val inputs: WeekendViewModelInputs = this
    val outputs: WeekendViewModelOutputs = this

    private val defaultTab = when (getDefaultSeasonUseCase.serverDefaultSeason) {
        currentSeasonYear -> WeekendNavItem.SCHEDULE
        else -> WeekendNavItem.RACE
    }

    private val selectedTab: MutableStateFlow<WeekendNavItem> = MutableStateFlow(defaultTab)
    private val seasonRound: MutableStateFlow<Pair<Int, Int>?> = MutableStateFlow(null)
    private val seasonRoundWithRequest: Flow<Pair<Int, Int>?> = seasonRound
        .filterNotNull()
        .flatMapLatest { (season, round) ->
            return@flatMapLatest flow {
                if (!raceRepository.hasntPreviouslySynced(season)) {
                    isRefreshing.postValue(true)
                    emit(null)
                    raceRepository.fetchRaces(season)
                    isRefreshing.postValue(false)
                    emit(Pair(season, round))
                }
                else {
                    emit(Pair(season, round))
                    isRefreshing.postValue(true)
                    raceRepository.fetchRaces(season)
                    isRefreshing.postValue(false)
                }
            }
        }
        .flowOn(ioDispatcher)

    private val raceFlow: Flow<Race?> = seasonRoundWithRequest
        .filterNotNull()
        .flatMapLatest { (season, round) -> raceRepository.getRace(season, round) }
        .flowOn(ioDispatcher)
        .shareIn(viewModelScope, started = SharingStarted.Lazily)

    override val weekendInfo: LiveData<WeekendInfo> = raceFlow
        .filterNotNull()
        .map { it.raceInfo.toWeekendInfo() }
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
        .flowOn(ioDispatcher)
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
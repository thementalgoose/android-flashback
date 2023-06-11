package tmg.flashback.weekend.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import tmg.flashback.domain.repo.RaceRepository
import tmg.flashback.formula1.constants.Formula1.currentSeasonYear
import tmg.flashback.formula1.model.Race
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.flashback.weekend.ui.WeekendNavItem.QUALIFYING
import tmg.flashback.weekend.ui.WeekendNavItem.RACE
import tmg.flashback.weekend.ui.WeekendNavItem.SCHEDULE
import tmg.utilities.extensions.combinePair
import javax.inject.Inject

interface WeekendViewModelInputs {
    fun load(season: Int, round: Int, date: LocalDate)
    fun clickTab(state: WeekendNavItem)
    fun refresh()
}

interface WeekendViewModelOutputs {
    val tabs: StateFlow<List<WeekendScreenState>>
    val isRefreshing: StateFlow<Boolean>
    val weekendInfo: StateFlow<ScreenWeekendData?>
}

@HiltViewModel
class WeekendViewModel @Inject constructor(
    private val raceRepository: RaceRepository,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel(), WeekendViewModelInputs, WeekendViewModelOutputs {

    val inputs: WeekendViewModelInputs = this
    val outputs: WeekendViewModelOutputs = this

    private val selectedTab: MutableStateFlow<WeekendNavItem> = MutableStateFlow(SCHEDULE)
    private val seasonRound: MutableStateFlow<Pair<Int, Int>?> = MutableStateFlow(null)
    private val seasonRoundWithRequest: Flow<Pair<Int, Int>?> = seasonRound
        .filterNotNull()
        .flatMapLatest { (season, round) ->
            return@flatMapLatest flow {
                if (!raceRepository.hasntPreviouslySynced(season)) {
                    isRefreshing.value = true
                    emit(null)
                    raceRepository.fetchRaces(season)
                    isRefreshing.value = false
                    emit(Pair(season, round))
                }
                else {
                    emit(Pair(season, round))
                    isRefreshing.value = true
                    raceRepository.fetchRaces(season)
                    isRefreshing.value = false
                }
            }
        }
        .flowOn(ioDispatcher)

    private val raceFlow: Flow<Race?> = seasonRoundWithRequest
        .filterNotNull()
        .flatMapLatest { (season, round) -> raceRepository.getRace(season, round) }

    override val weekendInfo: StateFlow<ScreenWeekendData?> = raceFlow
        .filterNotNull()
        .map { it.raceInfo.toWeekendInfo() }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    override val tabs: StateFlow<List<WeekendScreenState>> = seasonRound
        .filterNotNull()
        .flatMapLatest { (season, round) -> raceRepository.getRace(season, round) }
        .combinePair(selectedTab)
        .map { (race, navItem) ->
            val list = mutableListOf<WeekendScreenState>()
            list.add(WeekendScreenState(SCHEDULE, isSelected = navItem == SCHEDULE))
            list.add(WeekendScreenState(QUALIFYING, isSelected = navItem == QUALIFYING))
            if (race?.hasSprintQualifying == true) {
                list.add(WeekendScreenState(WeekendNavItem.SPRINT_QUALIFYING, isSelected = navItem == WeekendNavItem.SPRINT_QUALIFYING))
            }
            if (race?.hasSprintRace == true) {
                list.add(WeekendScreenState(WeekendNavItem.SPRINT, isSelected = navItem == WeekendNavItem.SPRINT))
            }
            list.add(WeekendScreenState(RACE, isSelected = navItem == RACE))
            return@map list
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, listOf(
            WeekendScreenState(SCHEDULE, true),
            WeekendScreenState(QUALIFYING, false),
            WeekendScreenState(RACE, false)
        ))

    override val isRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)

    override fun clickTab(state: WeekendNavItem) {
        selectedTab.value = state
    }

    override fun load(season: Int, round: Int, date: LocalDate) {
        val existing = seasonRound.value
        if (existing?.first != season || existing.second != round) {
            val now = LocalDate.now()
            selectedTab.value = when {
                season != currentSeasonYear -> RACE
                date == now -> RACE
                date == now.minusDays(1L) -> QUALIFYING
                date.isAfter(now) -> SCHEDULE
                else -> RACE
            }
            seasonRound.value = Pair(season, round)
        }
    }

    override fun refresh() {
        seasonRound.value?.first?.let { season ->
            isRefreshing.value = true
            viewModelScope.launch(ioDispatcher) {
                raceRepository.fetchRaces(season)
                isRefreshing.value = false
            }
        }
    }
}
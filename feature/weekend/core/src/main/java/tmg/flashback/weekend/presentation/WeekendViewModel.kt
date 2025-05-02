package tmg.flashback.weekend.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
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
import tmg.flashback.data.repo.RaceRepository
import tmg.flashback.data.repo.usecases.FetchSeasonUseCase
import tmg.flashback.formula1.model.Race
import tmg.flashback.reviews.usecases.AppSection.DETAILS_QUALIFYING
import tmg.flashback.reviews.usecases.AppSection.DETAILS_RACE
import tmg.flashback.reviews.usecases.ReviewSectionSeenUseCase
import tmg.flashback.weekend.navigation.ScreenWeekendData
import tmg.flashback.weekend.navigation.ScreenWeekendNav
import tmg.flashback.weekend.presentation.WeekendNavItem.QUALIFYING
import tmg.flashback.weekend.presentation.WeekendNavItem.RACE
import tmg.flashback.weekend.presentation.WeekendNavItem.SCHEDULE
import tmg.flashback.weekend.utils.getWeekendEventOrder
import tmg.utilities.extensions.combinePair
import tmg.utilities.extensions.toEnum
import java.lang.ClassCastException
import java.time.Year
import javax.inject.Inject

interface WeekendViewModelInputs {
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
    private val fetchSeasonUseCase: FetchSeasonUseCase,
    private val ioDispatcher: CoroutineDispatcher,
    private val reviewSectionSeenUseCase: ReviewSectionSeenUseCase,
): ViewModel(), WeekendViewModelInputs, WeekendViewModelOutputs {

    val inputs: WeekendViewModelInputs = this
    val outputs: WeekendViewModelOutputs = this

    private val defaultTab: WeekendNavItem = SCHEDULE

    private val selectedTab: MutableStateFlow<WeekendNavItem> = MutableStateFlow(defaultTab)
    private val seasonRound: MutableStateFlow<Pair<Int, Int>?> = MutableStateFlow(null)
    private val seasonRoundWithRequest: Flow<Pair<Int, Int>?> = seasonRound
        .filterNotNull()
        .flatMapLatest { (season, round) ->
            return@flatMapLatest flow {
                if (!raceRepository.hasntPreviouslySynced(season)) {
                    isRefreshing.value = true
                    emit(null)
                    fetchSeasonUseCase.fetchSeason(season)
                    isRefreshing.value = false
                    emit(Pair(season, round))
                }
                else {
                    emit(Pair(season, round))
                    isRefreshing.value = true
                    fetchSeasonUseCase.fetchSeason(season)
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
            getWeekendEventOrder(
                navItem = navItem,
                hasSprintQualifying = race?.hasSprintQualifying == true,
                hasSprintRace = race?.hasSprintRace == true,
                season = race?.raceInfo?.season ?: Year.now().value
            )
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, listOf(
            WeekendScreenState(SCHEDULE, defaultTab == SCHEDULE),
            WeekendScreenState(QUALIFYING, defaultTab == QUALIFYING),
            WeekendScreenState(RACE, defaultTab == RACE)
        ))

    override val isRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)

    override fun clickTab(state: WeekendNavItem) {
        selectedTab.value = state
        if (state == RACE) {
            reviewSectionSeenUseCase(DETAILS_RACE)
        }
        if (state == QUALIFYING) {
            reviewSectionSeenUseCase(DETAILS_QUALIFYING)
        }
    }

    fun load(season: Int, round: Int) {
        val existing = seasonRound.value
        if (existing?.first != season || existing.second != round) {
            seasonRound.value = Pair(season, round)
        }
    }

    override fun refresh() {
        seasonRound.value?.first?.let { season ->
            isRefreshing.value = true
            viewModelScope.launch(ioDispatcher) {
                fetchSeasonUseCase.fetchSeason(season)
                isRefreshing.value = false
            }
        }
    }
}
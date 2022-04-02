package tmg.flashback.statistics.ui.weekend

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.statistics.R
import tmg.flashback.statistics.repo.RaceRepository

interface WeekendViewModelInputs {
    fun loadRace(overviewRace: OverviewRace)
}

interface WeekendViewModelOutputs {
    val raceInfo: LiveData<RaceInfo>
    val tabs: LiveData<List<WeekendTabs>>
//    val schedule: LiveData<List<ScheduleCell>>
//    val qualifying: LiveData<List<ScheduleCell>>
}

class WeekendViewModel(
    private val raceRepository: RaceRepository
): ViewModel(), WeekendViewModelInputs, WeekendViewModelOutputs {

    val inputs: WeekendViewModelInputs = this
    val outputs: WeekendViewModelOutputs = this

    private val seasonRound: MutableStateFlow<Pair<Int, Int>?> = MutableStateFlow(null)
    private val raceInfoFlow: Flow<Race?> = seasonRound
        .filterNotNull()
        .flatMapLatest { (season, round) -> raceRepository.getRace(season, round) }
        .shareIn(viewModelScope, SharingStarted.Lazily)

    override val raceInfo: LiveData<RaceInfo> = raceInfoFlow
        .mapNotNull { it?.raceInfo }
        .asLiveData(viewModelScope.coroutineContext)

    override val tabs: LiveData<List<WeekendTabs>> = raceInfoFlow
        .map {
            val tabs = mutableListOf<WeekendTabs>()
            if (it?.schedule?.isNotEmpty() == true) {
                tabs.add(WeekendTabs.SCHEDULE)
            }
            tabs.add(WeekendTabs.QUALIFYING)
            if (it?.hasSprintQualifying == true) {
                tabs.add(when (it.raceInfo.season) {
                    2021 -> WeekendTabs.SPRINT_QUALIFYING
                    else -> WeekendTabs.SPRINT
                })
            }
            tabs.add(WeekendTabs.RACE)
            tabs.add(WeekendTabs.CONSTRUCTOR)
            return@map tabs
        }
        .asLiveData(viewModelScope.coroutineContext)

    override fun loadRace(overviewRace: OverviewRace) {
        seasonRound.value = Pair(overviewRace.season, overviewRace.round)
    }

    companion object {
        val defaultTabs = listOf(
            WeekendTabs.SCHEDULE,
            WeekendTabs.QUALIFYING,
            WeekendTabs.RACE,
            WeekendTabs.CONSTRUCTOR
        )
    }
}


enum class WeekendTabs(
    @StringRes val label: Int
) {
    SCHEDULE(R.string.weekend_tab_schedule),
    QUALIFYING(R.string.weekend_tab_qualifying),
    SPRINT_QUALIFYING(R.string.weekend_tab_sprint_qualifying),
    SPRINT(R.string.weekend_tab_sprint),
    RACE(R.string.weekend_tab_race),
    CONSTRUCTOR(R.string.weekend_tab_constructor),
}
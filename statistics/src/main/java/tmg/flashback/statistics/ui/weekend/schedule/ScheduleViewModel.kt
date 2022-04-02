package tmg.flashback.statistics.ui.weekend.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import tmg.flashback.formula1.enums.RaceWeekend
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.formula1.utils.NotificationUtils.getCategoryBasedOnLabel
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.statistics.repository.UpNextRepository

interface ScheduleViewModelInputs {
    fun load(season: Int, round: Int)
}

interface ScheduleViewModelOutputs {
    val list: LiveData<List<ScheduleModel>>
}

class ScheduleViewModel(
    private val raceRepository: RaceRepository,
    private val upNextRepository: UpNextRepository
): ViewModel(), ScheduleViewModelInputs, ScheduleViewModelOutputs {

    val inputs: ScheduleViewModelInputs = this
    val outputs: ScheduleViewModelOutputs = this

    private val seasonRound: MutableStateFlow<Pair<Int, Int>?> = MutableStateFlow(null)
    override val list: LiveData<List<ScheduleModel>> = seasonRound
        .filterNotNull()
        .flatMapLatest { (season, round) -> raceRepository.getRace(season, round) }
        .map { it?.schedule }
        .filterNotNull()
        .map { initialSchedule(it) }
        .asLiveData(viewModelScope.coroutineContext)

    override fun load(season: Int, round: Int) {
        seasonRound.value = Pair(season, round)
    }

    fun initialSchedule(models: List<Schedule>): List<ScheduleModel> {
        return models
            .groupBy { it.timestamp.deviceLocalDateTime.toLocalDate() }
            .toSortedMap()
            .map { (date, schedules) ->
                ScheduleModel(
                    date, schedules.map {
                        val notificationsEnabled = when (getCategoryBasedOnLabel(it.label)) {
                            RaceWeekend.FREE_PRACTICE -> upNextRepository.notificationFreePractice
                            RaceWeekend.QUALIFYING -> upNextRepository.notificationQualifying
                            RaceWeekend.RACE -> upNextRepository.notificationRace
                            null -> upNextRepository.notificationOther
                        }
                        Pair(it, notificationsEnabled)
                    }
                )
            }
    }
}
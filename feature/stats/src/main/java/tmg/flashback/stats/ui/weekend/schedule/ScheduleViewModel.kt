package tmg.flashback.stats.ui.weekend.schedule

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
import tmg.flashback.formula1.utils.NotificationUtils
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.stats.repository.NotificationRepository

interface ScheduleViewModelInputs {
    fun load(season: Int, round: Int)
}

interface ScheduleViewModelOutputs {
    val list: LiveData<List<ScheduleModel>>
}

class ScheduleViewModel(
    private val raceRepository: RaceRepository,
    private val notificationRepository: NotificationRepository
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

    private fun initialSchedule(models: List<Schedule>): List<ScheduleModel> {
        return models
            .groupBy { it.timestamp.deviceLocalDateTime.toLocalDate() }
            .toSortedMap()
            .map { (date, schedules) ->
                ScheduleModel(
                    date, schedules
                        .sortedBy { it.timestamp.utcLocalDateTime }
                        .map {
                            val notificationsEnabled = when (NotificationUtils.getCategoryBasedOnLabel(
                                it.label
                            )) {
                                RaceWeekend.FREE_PRACTICE -> notificationRepository.notificationFreePractice
                                RaceWeekend.QUALIFYING -> notificationRepository.notificationQualifying
                                RaceWeekend.RACE -> notificationRepository.notificationRace
                                null -> notificationRepository.notificationOther
                            }
                            Pair(it, notificationsEnabled)
                        }
                )
            }
    }
}
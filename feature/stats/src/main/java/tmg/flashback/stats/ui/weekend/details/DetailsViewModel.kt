package tmg.flashback.stats.ui.weekend.details

import android.webkit.URLUtil
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
import tmg.flashback.stats.R
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import tmg.utilities.models.StringHolder

interface DetailsViewModelInputs {
    fun load(season: Int, round: Int)
    fun linkClicked(model: DetailsModel.Link)
}

interface DetailsViewModelOutputs {
    val list: LiveData<List<DetailsModel>>
}

class DetailsViewModel(
    private val raceRepository: RaceRepository,
    private val notificationRepository: NotificationRepository,
    private val applicationNavigationComponent: ApplicationNavigationComponent
): ViewModel(), DetailsViewModelInputs, DetailsViewModelOutputs {

    val inputs: DetailsViewModelInputs = this
    val outputs: DetailsViewModelOutputs = this

    private val seasonRound: MutableStateFlow<Pair<Int, Int>?> = MutableStateFlow(null)
    override val list: LiveData<List<DetailsModel>> = seasonRound
        .filterNotNull()
        .flatMapLatest { (season, round) -> raceRepository.getRace(season, round) }
        .map {
            if (it == null) return@map emptyList()

            val list = mutableListOf<DetailsModel>()
            if (it.raceInfo.laps != null) {
                list.add(DetailsModel.Label(
                    label = StringHolder(R.string.details_link_laps, it.raceInfo.laps ?: ""),
                    icon = R.drawable.ic_details_laps,
                ))
            }
            if (it.raceInfo.youtube != null && URLUtil.isValidUrl(it.raceInfo.youtube)) {
                list.add(DetailsModel.Link(
                    label = R.string.details_link_youtube,
                    icon = R.drawable.ic_details_youtube,
                    url = it.raceInfo.youtube!!
                ))
            }
            if (it.raceInfo.wikipediaUrl != null && URLUtil.isValidUrl(it.raceInfo.wikipediaUrl)) {
                list.add(DetailsModel.Link(
                    label = R.string.details_link_wikipedia,
                    icon = R.drawable.ic_details_wikipedia,
                    url = it.raceInfo.wikipediaUrl!!
                ))
            }

            list.addAll(initialSchedule(it.schedule))
            return@map list
        }
        .asLiveData(viewModelScope.coroutineContext)

    override fun load(season: Int, round: Int) {
        seasonRound.value = Pair(season, round)
    }

    override fun linkClicked(model: DetailsModel.Link) {
        applicationNavigationComponent.openUrl(model.url)
    }

    private fun initialSchedule(models: List<Schedule>): List<DetailsModel> {
        return models
            .groupBy { it.timestamp.deviceLocalDateTime.toLocalDate() }
            .toSortedMap()
            .map { (date, schedules) ->
                DetailsModel.ScheduleDay(
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
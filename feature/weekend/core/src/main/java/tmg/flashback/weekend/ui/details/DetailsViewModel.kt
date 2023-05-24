package tmg.flashback.weekend.ui.details

import android.net.Uri
import android.webkit.URLUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import tmg.flashback.circuits.contract.Circuit
import tmg.flashback.circuits.contract.with
import tmg.flashback.formula1.enums.RaceWeekend
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.formula1.utils.NotificationUtils
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.results.contract.repository.NotificationsRepository
import tmg.flashback.domain.repo.RaceRepository
import tmg.flashback.weekend.R
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.utilities.models.StringHolder
import javax.inject.Inject

interface DetailsViewModelInputs {
    fun load(season: Int, round: Int)
    fun linkClicked(model: DetailsModel.Link)
}

interface DetailsViewModelOutputs {
    val list: LiveData<List<DetailsModel>>
}

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val raceRepository: RaceRepository,
    private val notificationRepository: NotificationsRepository,
    private val navigator: Navigator,
    private val openWebpageUseCase: OpenWebpageUseCase
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
            val links = mutableListOf<DetailsModel.Link>()

            links.add(DetailsModel.Link(
                label = R.string.details_link_circuit,
                icon = R.drawable.ic_details_track,
                url = getCircuitUrl(it.raceInfo.circuit.id, it.raceInfo.circuit.name)
            ))

            if (it.raceInfo.youtube != null && URLUtil.isValidUrl(it.raceInfo.youtube)) {
                links.add(
                    DetailsModel.Link(
                        label = R.string.details_link_youtube,
                        icon = R.drawable.ic_details_youtube,
                        url = it.raceInfo.youtube!!
                    )
                )
            }

            it.raceInfo.circuit.location?.let { location ->
                links.add(
                    DetailsModel.Link(
                        label = R.string.details_link_map,
                        icon = R.drawable.ic_details_maps,
                        url = "geo:${location.lat},${location.lng}?q=${Uri.encode(it.raceInfo.circuit.name)}"
                    )
                )
            }
            if (it.raceInfo.wikipediaUrl != null && URLUtil.isValidUrl(it.raceInfo.wikipediaUrl)) {
                links.add(
                    DetailsModel.Link(
                        label = R.string.details_link_wikipedia,
                        icon = R.drawable.ic_details_wikipedia,
                        url = it.raceInfo.wikipediaUrl!!
                    )
                )
            }
            list.add(DetailsModel.Links(links))

            list.add(DetailsModel.Track(
                circuit = it.raceInfo.circuit,
                raceName = it.raceInfo.name,
                season = it.raceInfo.season,
                laps = it.raceInfo.laps
            ))

            list.addAll(initialSchedule(it.schedule))

            return@map list
        }
        .asLiveData(viewModelScope.coroutineContext)

    override fun load(season: Int, round: Int) {
        seasonRound.value = Pair(season, round)
    }

    override fun linkClicked(model: DetailsModel.Link) {
        if (model.url.startsWith(URL_CIRCUIT_HISTORY)) {
            val parts = model.url.split("/")
            val circuitId = parts.getOrNull(parts.size - 2)
            val circuitName = parts.getOrNull(parts.size - 1)
            if (circuitId != null && circuitName != null) {
                navigator.navigate(Screen.Circuit.with(circuitId = circuitId, circuitName = circuitName))
            }
        } else {
            openWebpageUseCase.open(model.url, title = "")
        }
    }

    private fun initialSchedule(models: List<Schedule>): List<DetailsModel> {
        val dayGroupings = models
            .groupBy { it.timestamp.deviceLocalDateTime.toLocalDate() }
            .toSortedMap()
        return listOf(DetailsModel.ScheduleWeekend(
            days = dayGroupings.map { (date, schedules) ->
                date to schedules
                    .sortedBy { it.timestamp.utcLocalDateTime }
                    .map {
                        val notificationsEnabled =
                            when (NotificationUtils.getCategoryBasedOnLabel(
                                it.label
                            )) {
                                RaceWeekend.FREE_PRACTICE -> notificationRepository.notificationUpcomingFreePractice
                                RaceWeekend.QUALIFYING -> notificationRepository.notificationUpcomingQualifying
                                RaceWeekend.SPRINT -> notificationRepository.notificationUpcomingSprint
                                RaceWeekend.RACE -> notificationRepository.notificationUpcomingRace
                                null -> notificationRepository.notificationUpcomingOther
                            }
                        Pair(it, notificationsEnabled)
                    }
            }
        ))
    }

    companion object {
        private const val URL_CIRCUIT_HISTORY = "flashback://circuit-history"
        private fun getCircuitUrl(circuitId: String, circuitName: String): String {
            return "${URL_CIRCUIT_HISTORY}/$circuitId/$circuitName"
        }
    }
}
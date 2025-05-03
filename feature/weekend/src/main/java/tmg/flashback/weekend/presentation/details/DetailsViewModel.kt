package tmg.flashback.weekend.presentation.details

import android.webkit.URLUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import tmg.flashback.device.usecases.OpenLocationUseCase
import tmg.flashback.data.repo.RaceRepository
import tmg.flashback.formula1.enums.RaceWeekend
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.formula1.utils.NotificationUtils
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.season.contract.repository.NotificationsRepository
import tmg.flashback.formula1.model.notifications.NotificationUpcoming.FREE_PRACTICE
import tmg.flashback.formula1.model.notifications.NotificationUpcoming.OTHER
import tmg.flashback.formula1.model.notifications.NotificationUpcoming.QUALIFYING
import tmg.flashback.formula1.model.notifications.NotificationUpcoming.RACE
import tmg.flashback.formula1.model.notifications.NotificationUpcoming.SPRINT
import tmg.flashback.formula1.model.notifications.NotificationUpcoming.SPRINT_QUALIFYING
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.flashback.strings.R.string
import tmg.flashback.ui.R.drawable
import tmg.flashback.weekend.repository.WeatherRepository
import javax.inject.Inject

interface DetailsViewModelInputs {
    fun load(season: Int, round: Int)
    fun linkClicked(model: DetailsModel.Link)
}

interface DetailsViewModelOutputs {
    val list: StateFlow<List<DetailsModel>>
}

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val raceRepository: RaceRepository,
    private val notificationRepository: NotificationsRepository,
    private val navigator: Navigator,
    private val weatherRepository: WeatherRepository,
    private val openWebpageUseCase: OpenWebpageUseCase,
    private val openLocationUseCase: OpenLocationUseCase,
) : ViewModel(), DetailsViewModelInputs, DetailsViewModelOutputs {

    val inputs: DetailsViewModelInputs = this
    val outputs: DetailsViewModelOutputs = this

    private val seasonRound: MutableStateFlow<Pair<Int, Int>?> = MutableStateFlow(null)
    override val list: StateFlow<List<DetailsModel>> = seasonRound
        .filterNotNull()
        .flatMapLatest { (season, round) -> raceRepository.getRace(season, round) }
        .map {
            if (it == null) return@map emptyList()

            val list = mutableListOf<DetailsModel>()
            val links = mutableListOf<DetailsModel.Link>()

            links.add(
                DetailsModel.Link.Url(
                    label = string.details_link_circuit,
                    icon = drawable.ic_details_track,
                    url = getCircuitUrl(it.raceInfo.circuit.id, it.raceInfo.circuit.name)
                )
            )

            if (it.raceInfo.youtube != null && URLUtil.isValidUrl(it.raceInfo.youtube)) {
                links.add(
                    DetailsModel.Link.Url(
                        label = string.details_link_youtube,
                        icon = drawable.ic_details_youtube,
                        url = it.raceInfo.youtube!!
                    )
                )
            }

            it.raceInfo.circuit.location?.let { location ->
                links.add(
                    DetailsModel.Link.Location(
                        label = string.details_link_map,
                        icon = drawable.ic_details_maps,
                        lat = location.lat,
                        lng = location.lng,
                        name = it.raceInfo.circuit.name
                    )
                )
            }
            if (it.raceInfo.wikipediaUrl != null && URLUtil.isValidUrl(it.raceInfo.wikipediaUrl)) {
                links.add(
                    DetailsModel.Link.Url(
                        label = string.details_link_wikipedia,
                        icon = drawable.ic_details_wikipedia,
                        url = it.raceInfo.wikipediaUrl!!
                    )
                )
            }
            list.add(DetailsModel.Links(links))

            list.addAll(initialSchedule(it.schedule))

            list.add(
                DetailsModel.Track(
                    circuit = it.raceInfo.circuit,
                    raceName = it.raceInfo.name,
                    season = it.raceInfo.season,
                    laps = it.raceInfo.laps
                )
            )

            return@map list
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    override fun load(season: Int, round: Int) {
        seasonRound.value = Pair(season, round)
    }

    override fun linkClicked(model: DetailsModel.Link) {
        when (model) {
            is DetailsModel.Link.Location -> {
                openLocationUseCase.openLocation(
                    model.lat,
                    model.lng,
                    model.name
                )
            }

            is DetailsModel.Link.Url -> {
                if (model.url.startsWith(URL_CIRCUIT_HISTORY)) {
                    val parts = model.url.split("/")
                    val circuitId = parts.getOrNull(parts.size - 2)
                    val circuitName = parts.getOrNull(parts.size - 1)
                    if (circuitId != null && circuitName != null) {
                        navigator.navigate(
                            Screen.Circuit(
                                circuitId = circuitId,
                                circuitName = circuitName
                            )
                        )
                    }
                } else {
                    openWebpageUseCase.open(model.url, title = "")
                }
            }
        }
    }

    private fun initialSchedule(models: List<Schedule>): List<DetailsModel> {
        val dayGroupings = models
            .groupBy { it.timestamp.deviceLocalDateTime.toLocalDate() }
            .toSortedMap()
        if (dayGroupings.isEmpty()) {
            return emptyList()
        }
        return listOf(
            DetailsModel.ScheduleWeekend(
                days = dayGroupings.map { (date, schedules) ->
                    date to schedules
                        .sortedBy { it.timestamp.utcLocalDateTime }
                        .map {
                            val notificationsEnabled =
                                when (NotificationUtils.getCategoryBasedOnLabel(
                                    it.label
                                )) {
                                    RaceWeekend.FREE_PRACTICE -> notificationRepository.isUpcomingEnabled(
                                        FREE_PRACTICE
                                    )

                                    RaceWeekend.QUALIFYING -> notificationRepository.isUpcomingEnabled(
                                        QUALIFYING
                                    )

                                    RaceWeekend.SPRINT -> notificationRepository.isUpcomingEnabled(
                                        SPRINT
                                    )

                                    RaceWeekend.SPRINT_QUALIFYING -> notificationRepository.isUpcomingEnabled(
                                        SPRINT_QUALIFYING
                                    )

                                    RaceWeekend.RACE -> notificationRepository.isUpcomingEnabled(
                                        RACE
                                    )

                                    null -> notificationRepository.isUpcomingEnabled(OTHER)
                                }
                            Pair(it, notificationsEnabled)
                        }
                },
                temperatureMetric = weatherRepository.weatherTemperatureMetric,
                windspeedMetric = weatherRepository.weatherWindspeedMetric
            )
        )
    }

    companion object {
        private const val URL_CIRCUIT_HISTORY = "flashback://circuit-history"
        private fun getCircuitUrl(circuitId: String, circuitName: String): String {
            return "${URL_CIRCUIT_HISTORY}/$circuitId/$circuitName"
        }
    }
}
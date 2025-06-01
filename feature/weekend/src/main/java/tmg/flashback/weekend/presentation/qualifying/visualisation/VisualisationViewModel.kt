package tmg.flashback.weekend.presentation.qualifying.visualisation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import tmg.flashback.data.repo.RaceRepository
import tmg.flashback.formula1.model.QualifyingResult
import tmg.flashback.formula1.model.QualifyingRound
import tmg.flashback.formula1.model.QualifyingType
import tmg.flashback.weekend.presentation.qualifying.visualisation.VisualisationUiState.QualifyingDataNotAvailable
import tmg.flashback.weekend.presentation.qualifying.visualisation.VisualisationUiState.Visualisation
import tmg.utilities.extensions.secondsToHHmm
import tmg.utilities.extensions.secondsToHoursMinutes
import tmg.utilities.extensions.secondsToMinutesSeconds
import javax.inject.Inject
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

@HiltViewModel
class VisualisationViewModel @Inject constructor(
    private val raceRepository: RaceRepository
): ViewModel() {

    private val seasonRound: MutableStateFlow<Pair<Int, Int>?> = MutableStateFlow(null)
    private val type: MutableStateFlow<QualifyingType?> = MutableStateFlow(QualifyingType.Q3)

    val uiState: StateFlow<VisualisationUiState> = combine(
        type,
        seasonRound
            .filterNotNull()
            .flatMapLatest { (season, round) -> raceRepository.getRace(season, round) },
    ) { type, race ->
        if (type == null) {
            return@combine QualifyingDataNotAvailable
        }
        if (race?.qualifying == null) {
            return@combine QualifyingDataNotAvailable
        }

        val availableTypes = race.qualifying
            .map { it.label }
            .distinct()
            .sortedByDescending { it.order }

        val qualifying = race.qualifying
            .firstOrNull { it.label == type }
            ?.results
            ?.filter { it.lapTime != null }
            ?.sortedBy { it.position }
            ?: emptyList()

        if (qualifying.none { it.lapTime != null }) {
            return@combine QualifyingDataNotAvailable
        }

        val indicatorEntries = race.qualifying.calculateIndicatorEntries()

        return@combine Visualisation(
            availableQualifyingTypes = availableTypes,
            qualifyingType = type,
            resultEntries = qualifying.calculateResultEntries(
                indicatorEntries.first().millis
            ),
            indicatorEntries = indicatorEntries
        )
    }
        .stateIn(viewModelScope, SharingStarted.Lazily, QualifyingDataNotAvailable)

    fun selectType(type: QualifyingType) {
        this.type.value = type
    }

    fun load(season: Int, round: Int) {
        this.seasonRound.value = season to round
    }

    private fun List<QualifyingResult>.calculateResultEntries(
        minimumQualifyingMillis: Int
    ): List<ResultEntry> {
        return this.mapNotNull {
            val lapTime = it.lapTime ?: return@mapNotNull null
            ResultEntry(
                driverEntry = it.entry,
                normalisedQualifyingMillis = lapTime.totalMillis - minimumQualifyingMillis
            )
        }
    }

    private fun List<QualifyingRound>.calculateIndicatorEntries(): List<IndicatorEntry> {
        val validTimestamps = this.flatMap { it.results }.mapNotNull { it.lapTime }
        val minSeconds = floor(validTimestamps.minOf { it.totalMillis } / 1000f).roundToInt()
        val maxSeconds = ceil(validTimestamps.maxOf { it.totalMillis } / 1000f).roundToInt()
        val indicators = (maxSeconds - minSeconds) + 1

        return List(indicators) {
            val seconds = minSeconds + it
            IndicatorEntry(
                millis = seconds * 1000,
                normalizedMillis = it * 1000,
                label = seconds.secondsToMinutesSeconds(extendMinutes = false)
            )
        }
    }
}
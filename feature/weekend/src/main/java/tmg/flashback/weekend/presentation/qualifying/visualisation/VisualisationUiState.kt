package tmg.flashback.weekend.presentation.qualifying.visualisation

import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.formula1.model.QualifyingType

sealed class VisualisationUiState {
    data object Loading: VisualisationUiState()
    data object QualifyingDataNotAvailable: VisualisationUiState()
    data class Visualisation(
        val availableQualifyingTypes: List<QualifyingType>,
        val qualifyingType: QualifyingType,
        val indicatorEntries: List<IndicatorEntry>,
        val resultEntries: List<ResultEntry>
    ): VisualisationUiState()
}

data class IndicatorEntry(
    val normalizedMillis: Int,
    val millis: Int,
    val label: String?
)

data class ResultEntry(
    val driverEntry: DriverEntry,
    val normalisedQualifyingMillis: Int
)
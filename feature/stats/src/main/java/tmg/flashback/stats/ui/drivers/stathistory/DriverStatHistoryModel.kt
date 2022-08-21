package tmg.flashback.stats.ui.drivers.stathistory

import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.RaceInfo

sealed class DriverStatHistoryModel(
    val key: String
) {
    data class Year(
        val season: Int
    ): DriverStatHistoryModel(
        key = "year-$season"
    )

    data class Label(
        val text: String
    ): DriverStatHistoryModel(
        key = "label-$text"
    )

    data class Race(
        val raceInfo: RaceInfo,
        val constructor: Constructor?
    ): DriverStatHistoryModel(
        key = "race-${raceInfo.season}-${raceInfo.round}"
    )

    data class RacePosition(
        val position: Int,
        val raceInfo: RaceInfo,
        val constructor: Constructor?
    ): DriverStatHistoryModel(
        key = "race-position-$position-${raceInfo.season}-${raceInfo.round}"
    )

    object Empty: DriverStatHistoryModel(key = "empty")

    companion object
}
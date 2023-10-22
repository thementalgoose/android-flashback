package tmg.flashback.season.presentation.dashboard.drivers

import tmg.flashback.formula1.model.SeasonDriverStandingSeason

data class DriverStandingsScreenState(
    val season: Int,
    val standings: List<SeasonDriverStandingSeason> = emptyList(),
    val inProgress: Pair<String, Int>? = null,
    val isLoading: Boolean = true,
    val networkAvailable: Boolean = true,
    val maxPoints: Double = 0.0,
    val currentlySelected: SeasonDriverStandingSeason? = null
)
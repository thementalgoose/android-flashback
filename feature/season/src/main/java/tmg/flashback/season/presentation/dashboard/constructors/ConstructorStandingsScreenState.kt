package tmg.flashback.season.presentation.dashboard.constructors

import tmg.flashback.formula1.model.SeasonConstructorStandingSeason

data class ConstructorStandingsScreenState(
    val season: Int,
    val standings: List<SeasonConstructorStandingSeason> = emptyList(),
    val inProgress: Pair<String, Int>? = null,
    val isLoading: Boolean = false,
    val networkAvailable: Boolean = true,
    val maxPoints: Double = 0.0,
    val currentlySelected: SeasonConstructorStandingSeason? = null
)
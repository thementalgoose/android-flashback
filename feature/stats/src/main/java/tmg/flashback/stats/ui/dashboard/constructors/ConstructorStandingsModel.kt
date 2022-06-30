package tmg.flashback.stats.ui.dashboard.constructors

import tmg.flashback.formula1.model.SeasonConstructorStandingSeason

sealed class ConstructorStandingsModel(
    val id: String
) {
    data class Standings(
        val standings: SeasonConstructorStandingSeason,
        val isSelected: Boolean = false,
    ): ConstructorStandingsModel(
        id = standings.constructor.id
    )

    object Loading: ConstructorStandingsModel("loading")
}
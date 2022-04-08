package tmg.flashback.stats.ui.dashboard.constructors

import tmg.flashback.formula1.model.SeasonConstructorStandingSeason

data class ConstructorStandingsModel(
    val standings: SeasonConstructorStandingSeason,
    val isSelected: Boolean = false,
    val id: String = standings.constructor.id
) {
}
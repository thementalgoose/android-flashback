package tmg.flashback.stats.ui.dashboard.drivers

import tmg.flashback.formula1.model.SeasonDriverStandingSeason

sealed class DriverStandingsModel(
    val id: String
) {
    data class Standings(
        val standings: SeasonDriverStandingSeason,
        val isSelected: Boolean = false
    ): DriverStandingsModel(
        id = standings.driver.id
    )

    object Loading: DriverStandingsModel(
        id = "loading"
    )
}

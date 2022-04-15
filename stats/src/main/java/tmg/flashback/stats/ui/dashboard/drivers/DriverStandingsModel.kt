package tmg.flashback.stats.ui.dashboard.drivers

import tmg.flashback.formula1.model.SeasonDriverStandingSeason

data class DriverStandingsModel(
    val standings: SeasonDriverStandingSeason,
    val isSelected: Boolean = false,
    val id: String = standings.driver.id
)
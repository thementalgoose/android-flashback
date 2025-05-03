package tmg.flashback.drivers.presentation.overview

import tmg.flashback.formula1.model.Driver

data class DriverOverviewScreenState(
    val driverId: String,
    val driverName: String,
    val driver: Driver? = null,
    val list: List<DriverOverviewModel> = emptyList(),
    val isLoading: Boolean = false,
    val networkAvailable: Boolean = false,
    val selectedSeason: Int? = null
)
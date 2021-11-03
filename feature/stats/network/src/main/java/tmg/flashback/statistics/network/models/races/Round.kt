package tmg.flashback.statistics.network.models.races

import kotlinx.serialization.Serializable
import tmg.flashback.statistics.network.models.constructors.ConstructorData
import tmg.flashback.statistics.network.models.drivers.DriverData

@Serializable
data class Round(
    val drivers: Map<String, DriverData>,
    val constructors: Map<String, ConstructorData>,
    val data: RaceData,
    val race: Map<String, RaceResult>?,
    val qualifying: Map<String, QualifyingResult>?
)
package tmg.flashback.statistics.network.models.constructors

import tmg.flashback.statistics.network.models.drivers.DriverData

typealias AllConstructors = Map<String, ConstructorData>

data class ConstructorData(
    val id: String,
    val colour: String,
    val name: String,
    val nationality: String,
    val nationalityISO: String,
    val wikiUrl: String?
)
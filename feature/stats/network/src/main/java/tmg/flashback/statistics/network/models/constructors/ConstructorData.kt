package tmg.flashback.statistics.network.models.constructors

import kotlinx.serialization.Serializable
import tmg.flashback.statistics.network.models.drivers.DriverData

@Serializable
class AllConstructors: HashMap<String, ConstructorData>()

@Serializable
data class ConstructorData(
    val id: String,
    val colour: String,
    val name: String,
    val nationality: String,
    val nationalityISO: String,
    val wikiUrl: String?
)
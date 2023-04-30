package tmg.flashback.statistics.network.models.constructors

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import tmg.flashback.statistics.network.models.drivers.Driver

@Keep
@Serializable
data class ConstructorHistory(
    val construct: Constructor,
    val standings: Map<String, ConstructorHistoryStanding>
) {
    companion object
}

@Keep
@Serializable
data class ConstructorHistoryStanding(
    val season: Int,
    val championshipPosition: Int? = null,
    val points: Double? = 0.0,
    val inProgress: Boolean,
    val races: Int? = 0,
    val drivers: Map<String, ConstructorHistoryStandingDriver>
) {
    companion object
}

@Keep
@Serializable
data class ConstructorHistoryStandingDriver(
    val driver: Driver,
    val points: Double,
    val wins: Int? = null,
    val races: Int? = null,
    val podiums: Int? = null,
    val pointsFinishes: Int? = null,
    val pole: Int? = null,
    val championshipPosition: Int? = null
) {
    companion object
}
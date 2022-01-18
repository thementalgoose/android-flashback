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
    val championshipPosition: Int?,
    val points: Double?,
    val inProgress: Boolean,
    val races: Int?,
    val drivers: Map<String, ConstructorHistoryStandingDriver>
) {
    companion object
}

@Keep
@Serializable
data class ConstructorHistoryStandingDriver(
    val driver: Driver,
    val points: Double,
    val wins: Int?,
    val races: Int?,
    val podiums: Int?,
    val pointsFinishes: Int?,
    val pole: Int?,
    val championshipPosition: Int?
) {
    companion object
}
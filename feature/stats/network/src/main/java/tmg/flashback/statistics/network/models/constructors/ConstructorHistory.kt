package tmg.flashback.statistics.network.models.constructors

import kotlinx.serialization.Serializable
import tmg.flashback.statistics.network.models.drivers.Driver

@Serializable
data class ConstructorHistory(
    val construct: Constructor,
    val standings: Map<String, ConstructorHistoryStanding>
) {
    companion object
}

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
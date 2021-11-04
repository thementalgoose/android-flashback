package tmg.flashback.statistics.network.models.races

import kotlinx.serialization.Serializable

@Serializable
data class DriverStandings(
    val driverId: String,
    val points: Double,
    val inProgress: Boolean? = null,
    val races: Int,
    val position: Int? = null,
    val constructors: Map<String, Double>
) {
    companion object
}
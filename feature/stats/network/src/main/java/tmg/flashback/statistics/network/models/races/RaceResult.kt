package tmg.flashback.statistics.network.models.races

import kotlinx.serialization.Serializable

@Serializable
data class RaceResult(
    val driverId: String,
    val driverNumber: String,
    val constructorId: String,
    val points: Double,
    val qualified: Int? = null,
    val gridPos: Int? = null,
    val finished: Int,
    val status: String,
    val time: String? = null,
    val fastestLap: FastestLap?
) {
    companion object
}
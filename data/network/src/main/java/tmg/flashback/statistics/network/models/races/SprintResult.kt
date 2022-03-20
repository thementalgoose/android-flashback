package tmg.flashback.statistics.network.models.races

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class SprintResult(
    val driverId: String,
    val driverNumber: String,
    val constructorId: String,
    val points: Double,
    val gridPos: Int? = null,
    val finished: Int,
    val status: String,
    val time: String? = null
) {
    companion object
}
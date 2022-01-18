package tmg.flashback.statistics.network.models.races

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class DriverStandings(
    val driverId: String,
    val points: Double,
    val inProgress: Boolean? = null,
    val inProgressInfo: InProgressInfo? = null,
    val races: Int,
    val position: Int? = null,
    val constructors: Map<String, Double>
) {
    companion object
}
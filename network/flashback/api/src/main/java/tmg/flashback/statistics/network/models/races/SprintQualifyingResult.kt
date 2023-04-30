package tmg.flashback.statistics.network.models.races

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class SprintQualifyingResult(
    val driverId: String,
    val driverNumber: String?,
    val constructorId: String,
    val qualified: Int,
    val sq1: String? = null,
    val sq2: String? = null,
    val sq3: String? = null
)
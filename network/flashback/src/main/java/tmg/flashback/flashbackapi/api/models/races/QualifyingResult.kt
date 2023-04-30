package tmg.flashback.flashbackapi.api.models.races

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class QualifyingResult(
    val driverId: String,
    val driverNumber: String?,
    val constructorId: String,
    val points: Double? = null,
    val qualified: Int,
    val q1: String? = null,
    val q2: String? = null,
    val q3: String? = null
) {
    companion object
}
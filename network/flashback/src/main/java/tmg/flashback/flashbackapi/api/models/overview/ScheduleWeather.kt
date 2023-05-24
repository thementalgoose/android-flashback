package tmg.flashback.flashbackapi.api.models.overview

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class ScheduleWeather(
    val rainPercent: Double,
    val windMs: Double,
    val windBearing: Int,
    val tempMaxC: Double,
    val tempMinC: Double,
    val summary: List<String>
) {
    companion object
}
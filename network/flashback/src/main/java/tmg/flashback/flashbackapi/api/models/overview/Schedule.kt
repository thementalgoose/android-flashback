package tmg.flashback.flashbackapi.api.models.overview

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Schedule(
    val label: String,
    val date: String,
    val time: String,
    val weather: ScheduleWeather?
) {
    companion object
}
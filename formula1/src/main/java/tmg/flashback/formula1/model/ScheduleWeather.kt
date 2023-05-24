package tmg.flashback.formula1.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScheduleWeather(
    val rainPercent: Double,
    val windMs: Double,
    val windBearing: Int,
    val tempMaxC: Double,
    val tempMinC: Double,
    val summary: List<WeatherType>
): Parcelable {

    @IgnoredOnParcel
    val windMph: Double by lazy { windMs * 2.23694 }

    companion object
}
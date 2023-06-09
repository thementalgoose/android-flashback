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

    @IgnoredOnParcel
    val windKph: Double by lazy { windMs * 3.6 }

    @IgnoredOnParcel
    val tempAverageC: Double by lazy {
        (tempMinC + ((tempMaxC - tempMinC) / 2))
    }

    @IgnoredOnParcel
    val tempMaxF: Double by lazy { tempMaxC.toF() }

    @IgnoredOnParcel
    val tempMinF: Double by lazy { tempMinC.toF() }

    @IgnoredOnParcel
    val tempAverageF: Double by lazy { tempAverageC.toF() }

    private fun Double.toF(): Double {
        return (this * 1.8) + 32.0
    }

    companion object
}
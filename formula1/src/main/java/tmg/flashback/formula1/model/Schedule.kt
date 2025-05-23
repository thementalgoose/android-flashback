package tmg.flashback.formula1.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime

@Parcelize
data class Schedule(
    val label: String,
    val date: LocalDate,
    val time: LocalTime,
    val weather: ScheduleWeather?
): Parcelable {

    @IgnoredOnParcel
    val timestamp: Timestamp by lazy {
        Timestamp(date, time)
    }

    companion object
}
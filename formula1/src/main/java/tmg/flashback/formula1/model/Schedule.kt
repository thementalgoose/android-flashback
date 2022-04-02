package tmg.flashback.formula1.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

@Parcelize
data class Schedule(
    val label: String,
    val date: LocalDate,
    val time: LocalTime
): Parcelable {

    val timestamp: Timestamp by lazy {
        Timestamp(date, time)
    }

    companion object
}
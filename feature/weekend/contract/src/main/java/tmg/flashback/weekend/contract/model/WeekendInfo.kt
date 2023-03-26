package tmg.flashback.weekend.contract.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.utilities.extensions.toLocalDate

@Parcelize
@Serializable
data class WeekendInfo(
    val season: Int,
    val round: Int,
    val raceName: String,
    val circuitId: String,
    val circuitName: String,
    val country: String,
    val countryISO: String,
    val dateString: String
): Parcelable {

    val date: LocalDate
        get() = dateString.toLocalDate("yyyy-MM-dd")!!

    constructor(
        season: Int,
        round: Int,
        raceName: String,
        circuitId: String,
        circuitName: String,
        country: String,
        countryISO: String,
        date: LocalDate
    ): this(
        season = season,
        round = round,
        raceName = raceName,
        circuitId = circuitId,
        circuitName = circuitName,
        country = country,
        countryISO = countryISO,
        dateString = DateTimeFormatter
            .ofPattern("yyyy-MM-dd")
            .format(date)
    )

    companion object
}
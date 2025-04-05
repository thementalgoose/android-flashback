package tmg.flashback.weekend.contract.model

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import tmg.flashback.formula1.model.OverviewRace
import tmg.utilities.extensions.toLocalDate

@Parcelize
@Serializable
data class ScreenWeekendData(
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
        overviewRace: OverviewRace
    ): this(
        season = overviewRace.season,
        round = overviewRace.round,
        raceName = overviewRace.raceName,
        circuitId = overviewRace.circuitId,
        circuitName = overviewRace.circuitName,
        country = overviewRace.country,
        countryISO = overviewRace.countryISO,
        date = overviewRace.date
    )

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


    companion object NavType: androidx.navigation.NavType<ScreenWeekendData>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): ScreenWeekendData? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(key, ScreenWeekendData::class.java)
            } else {
                bundle.getParcelable(key)
            }
        }
        override fun parseValue(value: String): ScreenWeekendData {
            return Json.decodeFromString(serializer(), value)
        }
        override fun put(bundle: Bundle, key: String, value: ScreenWeekendData) {
            bundle.putParcelable(key, value)
        }
    }
}
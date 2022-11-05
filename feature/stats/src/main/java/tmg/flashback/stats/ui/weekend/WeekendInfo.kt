package tmg.flashback.stats.ui.weekend

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.model.RaceInfo
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
        get() = dateString.toLocalDate("yyyy/MM/dd")!!

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
            .ofPattern("yyyy/MM/dd")
            .format(date)
    )

    companion object
}

class WeekendInfoType : NavType<WeekendInfo>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): WeekendInfo? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, WeekendInfo::class.java)
        } else {
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): WeekendInfo {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: WeekendInfo) {
        bundle.putParcelable(key, value)
    }
}

fun RaceInfo.toWeekendInfo(): WeekendInfo {
    return WeekendInfo.from(this)
}

fun WeekendInfo.Companion.from(raceInfo: RaceInfo): WeekendInfo {
    return WeekendInfo(
        season = raceInfo.season,
        round = raceInfo.round,
        raceName = raceInfo.name,
        circuitId = raceInfo.circuit.id,
        circuitName = raceInfo.circuit.name,
        country = raceInfo.circuit.country,
        countryISO = raceInfo.circuit.countryISO,
        date = raceInfo.date,
    )
}

internal val fakeWeekendInfo: WeekendInfo = WeekendInfo(
    season = 2020,
    round = 1,
    raceName = "Testing Grand Prix",
    circuitId = "silverstone",
    circuitName = "Silverstone",
    country = "Country",
    countryISO = "GB",
    date = LocalDate.of(2020, 1, 1)
)
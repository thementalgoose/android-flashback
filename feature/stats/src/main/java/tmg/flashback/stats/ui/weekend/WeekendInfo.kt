package tmg.flashback.stats.ui.weekend

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.RaceInfo

@Parcelize
data class WeekendInfo(
    val season: Int,
    val round: Int,
    val raceName: String,
    val circuitId: String,
    val circuitName: String,
    val country: String,
    val countryISO: String,
    val laps: String? = null,
    val date: LocalDate
): Parcelable {
    companion object
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
        laps = raceInfo.laps,
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
    laps = "57",
    date = LocalDate.of(2020, 1, 1)
)
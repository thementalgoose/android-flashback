package tmg.flashback.weekend.ui

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.weekend.contract.model.WeekendInfo

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
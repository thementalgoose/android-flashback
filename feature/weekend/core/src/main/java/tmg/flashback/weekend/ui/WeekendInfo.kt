package tmg.flashback.weekend.ui

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.weekend.contract.model.ScreenWeekendData

class WeekendInfoType : NavType<ScreenWeekendData>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): ScreenWeekendData? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, ScreenWeekendData::class.java)
        } else {
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): ScreenWeekendData {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: ScreenWeekendData) {
        bundle.putParcelable(key, value)
    }
}

fun RaceInfo.toWeekendInfo(): ScreenWeekendData {
    return ScreenWeekendData(
        season = this.season,
        round = this.round,
        raceName = this.name,
        circuitId = this.circuit.id,
        circuitName = this.circuit.name,
        country = this.circuit.country,
        countryISO = this.circuit.countryISO,
        date = this.date,
    )
}

internal val fakeWeekendInfo: ScreenWeekendData = ScreenWeekendData(
    season = 2020,
    round = 1,
    raceName = "Testing Grand Prix",
    circuitId = "silverstone",
    circuitName = "Silverstone",
    country = "Country",
    countryISO = "GB",
    date = LocalDate.of(2020, 1, 1)
)
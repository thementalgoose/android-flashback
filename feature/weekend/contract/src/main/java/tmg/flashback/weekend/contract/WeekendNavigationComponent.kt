package tmg.flashback.weekend.contract

import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen
import tmg.flashback.weekend.contract.model.WeekendInfo

@JvmInline
value class ScreenWeekend(val route: String)
val Screen.Weekend get() = ScreenWeekend("weekend/{season}/{round}?" +
        "raceName={raceName}" + "&" +
        "circuitId={circuitId}" + "&" +
        "circuitName={circuitName}" + "&" +
        "country={country}" + "&" +
        "countryISO={countryISO}" + "&" +
        "date={date}"
)
fun ScreenWeekend.with(weekendInfo: WeekendInfo) = NavigationDestination(
    this@with.route
        .replace("{season}", weekendInfo.season.toString())
        .replace("{round}", weekendInfo.round.toString())
        .replace("{raceName}", weekendInfo.raceName)
        .replace("{circuitId}", weekendInfo.circuitId)
        .replace("{circuitName}", weekendInfo.circuitName)
        .replace("{country}", weekendInfo.country)
        .replace("{countryISO}", weekendInfo.countryISO)
        .replace("{date}", weekendInfo.dateString)
)
package tmg.flashback.weekend.contract

import kotlinx.serialization.json.Json
import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen
import tmg.flashback.weekend.contract.model.ScreenWeekendData

@JvmInline
value class ScreenWeekend(val route: String)
val Screen.Weekend get() = ScreenWeekend("weekend/{data}")

fun ScreenWeekend.with(weekendInfo: ScreenWeekendData) = NavigationDestination(
    this@with.route.replace("{data}", Json.encodeToString(ScreenWeekendData.serializer(), weekendInfo))
)
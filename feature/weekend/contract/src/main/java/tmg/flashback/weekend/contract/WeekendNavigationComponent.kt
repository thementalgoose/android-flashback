package tmg.flashback.weekend.contract

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen
import tmg.flashback.weekend.contract.model.ScreenWeekendData

@JvmInline
value class ScreenWeekend(val route: String)
val Screen.Weekend get() = ScreenWeekend("weekend/{screenWeekendData}")

fun ScreenWeekend.with(weekendInfo: ScreenWeekendData) = NavigationDestination(
    this@with.route.replace("{screenWeekendData}", Json.encodeToString(ScreenWeekendData.serializer(), weekendInfo))
)
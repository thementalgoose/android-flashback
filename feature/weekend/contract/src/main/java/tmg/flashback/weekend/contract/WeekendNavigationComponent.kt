package tmg.flashback.weekend.contract

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import kotlinx.serialization.json.Json
import org.threeten.bp.Year
import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.flashback.weekend.contract.model.ScreenWeekendNav
import java.lang.RuntimeException
import kotlin.jvm.Throws

@JvmInline
value class ScreenWeekend(val route: String) {
    companion object {
        const val DATA = "data"
        const val TAB = "tab"
    }
}
val Screen.Weekend get() = ScreenWeekend("weekend/{data}?tab={tab}")

fun ScreenWeekend.with(weekendInfo: ScreenWeekendData, tab: ScreenWeekendNav? = weekendInfo.getTab()) = NavigationDestination(
    this@with.route
        .replace("{data}", Json.encodeToString(ScreenWeekendData.serializer(), weekendInfo))
        .replace("{tab}", tab?.name ?: "")
)

// TODO: This is only around for purposes of deeplinking from widgets. Look at removing
@Throws(RuntimeException::class)
fun String.stripWeekendJsonData(): String {
    return this.split("?tab=")[0].replace("weekend/", "")
}


private fun ScreenWeekendData.getTab(): ScreenWeekendNav {
    return when {
        season > Year.now().value -> ScreenWeekendNav.SCHEDULE
        season < Year.now().value -> ScreenWeekendNav.RACE
        else -> ScreenWeekendNav.SCHEDULE
    }
}

interface WeekendNavigationComponent {

    @Composable
    fun Weekend(
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass,
        weekendData: ScreenWeekendData,
    )
}
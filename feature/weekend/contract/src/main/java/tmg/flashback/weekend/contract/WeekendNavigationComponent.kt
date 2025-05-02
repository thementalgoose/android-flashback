package tmg.flashback.weekend.contract

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import kotlinx.serialization.json.Json
import java.time.Year
import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.flashback.weekend.contract.model.ScreenWeekendNav
import java.lang.RuntimeException
import kotlin.jvm.Throws

interface WeekendNavigationComponent {

    @Composable
    fun Weekend(
        paddingValues: PaddingValues,
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass,
        weekendData: ScreenWeekendData,
    )
}
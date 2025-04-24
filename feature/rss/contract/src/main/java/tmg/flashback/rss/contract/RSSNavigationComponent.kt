package tmg.flashback.rss.contract

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen

val Screen.RSS: NavigationDestination
    get() = NavigationDestination("rss", launchSingleTop = true, popUpTo = "results/races")

interface RSSNavigationComponent {

    @Composable
    fun Configure(
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass,
        paddingValues: PaddingValues,
    )
}
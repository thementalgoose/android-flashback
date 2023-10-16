package tmg.flashback.rss.contract

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen

val Screen.Settings.RSSConfigure: NavigationDestination
    get() = NavigationDestination(
        "settings/rss/configure",
        popUpTo = "settings/rss"
    )

val Screen.RSS: NavigationDestination
    get() = NavigationDestination("rss", launchSingleTop = true)

interface RSSNavigationComponent {

    @Composable
    fun Configure(
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass
    )
}
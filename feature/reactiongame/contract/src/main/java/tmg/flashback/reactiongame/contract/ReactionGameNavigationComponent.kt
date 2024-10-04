package tmg.flashback.reactiongame.contract

import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen

val Screen.Reaction: NavigationDestination
    get() = NavigationDestination("reaction", launchSingleTop = true)
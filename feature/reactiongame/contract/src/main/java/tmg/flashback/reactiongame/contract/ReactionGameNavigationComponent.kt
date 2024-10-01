package tmg.flashback.reactiongame.contract

import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen

val Screen.ReactionGame: NavigationDestination
    get() = NavigationDestination("reaction-game", launchSingleTop = true)
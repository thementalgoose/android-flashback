package tmg.flashback.season.contract

import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen

val Screen.DriverStandings get() = NavigationDestination("results/drivers")

val Screen.ConstructorsStandings get() = NavigationDestination("results/constructors")

val Screen.Races get() = NavigationDestination("results/races")

interface ResultsNavigationComponent {

    fun tyres(season: Int)

    fun preseasonEvents(season: Int)
}
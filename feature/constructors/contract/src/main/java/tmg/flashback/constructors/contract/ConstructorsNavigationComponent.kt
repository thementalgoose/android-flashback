package tmg.flashback.constructors.contract

import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen

@JvmInline
value class ScreenConstructor(val route: String)
val Screen.Constructor get() = ScreenConstructor("constructors/{constructorId}?constructorName={constructorName}")
fun ScreenConstructor.with(
    constructorId: String,
    constructorName: String
) = NavigationDestination(
    this@with.route
        .replace("{constructorId}", constructorId)
        .replace("{constructorName}", constructorName)
)

@JvmInline
value class ScreenConstructorSeason(val route: String)
val Screen.ConstructorSeason get() = ScreenConstructorSeason("constructors/{constructorId}/{season}?constructorName={constructorName}")
fun ScreenConstructorSeason.with(
    constructorId: String,
    constructorName: String,
    season: Int
) = NavigationDestination(
    this@with.route
        .replace("{constructorId}", constructorId)
        .replace("{constructorName}", constructorName)
        .replace("{season}", season.toString())
)

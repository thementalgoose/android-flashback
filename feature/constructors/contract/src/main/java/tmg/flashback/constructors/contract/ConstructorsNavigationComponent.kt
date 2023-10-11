package tmg.flashback.constructors.contract

import kotlinx.serialization.json.Json
import tmg.flashback.constructors.contract.model.ScreenConstructorData
import tmg.flashback.constructors.contract.model.ScreenConstructorSeasonData
import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen

val Screen.ConstructorsStandings get() = NavigationDestination("drivers")

@JvmInline
value class ScreenConstructor(val route: String)
val Screen.Constructor get() = ScreenConstructor("constructors/{data}")
fun ScreenConstructor.with(
    constructorId: String,
    constructorName: String
) = NavigationDestination(
    this@with.route.replace("{data}", Json.encodeToString(ScreenConstructorData.serializer(), ScreenConstructorData(constructorId, constructorName)))
)

@JvmInline
value class ScreenConstructorSeason(val route: String)
val Screen.ConstructorSeason get() = ScreenConstructorSeason("constructors-season/{data}")
fun ScreenConstructorSeason.with(
    constructorId: String,
    constructorName: String,
    season: Int
) = NavigationDestination(
    this@with.route.replace("{data}", Json.encodeToString(ScreenConstructorSeasonData.serializer(), ScreenConstructorSeasonData(constructorId, constructorName, season)))
)

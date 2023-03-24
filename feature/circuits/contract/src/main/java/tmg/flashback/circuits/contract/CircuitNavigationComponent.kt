package tmg.flashback.circuits.contract

import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen

@JvmInline
value class ScreenCircuit(val route: String)
val Screen.Circuit get() = ScreenCircuit("circuit/{circuitId}?circuitName={circuitName}")
fun ScreenCircuit.with(
    circuitId: String,
    circuitName: String
) = NavigationDestination(
    this@with.route
        .replace("{circuitId}", circuitId)
        .replace("{circuitName}", circuitName)
)
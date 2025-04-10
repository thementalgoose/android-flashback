package tmg.flashback.circuits.contract

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import kotlinx.serialization.json.Json
import tmg.flashback.circuits.contract.model.ScreenCircuitData
import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen

@JvmInline
value class ScreenCircuit(val route: String)
val Screen.Circuit get() = ScreenCircuit("circuit/{data}")
fun ScreenCircuit.with(
    circuitId: String,
    circuitName: String
) = NavigationDestination(
    this@with.route
        .replace("{data}", Json.encodeToString(ScreenCircuitData.serializer(), ScreenCircuitData(circuitId, circuitName)))
)

interface CircuitNavigationComponent {

    @Composable
    fun CircuitScreen(
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass,
        circuitId: String,
        circuitName: String
    )
}
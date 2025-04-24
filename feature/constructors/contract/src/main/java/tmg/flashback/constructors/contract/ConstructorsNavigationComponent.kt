package tmg.flashback.constructors.contract

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import kotlinx.serialization.json.Json
import tmg.flashback.constructors.contract.model.ScreenConstructorData
import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen

@JvmInline
value class ScreenConstructor(val route: String)
val Screen.Constructor get() = ScreenConstructor("constructors/{data}")
fun ScreenConstructor.with(
    constructorId: String,
    constructorName: String
) = NavigationDestination(
    this@with.route.replace("{data}", Json.encodeToString(ScreenConstructorData.serializer(), ScreenConstructorData(constructorId, constructorName)))
)

interface ConstructorsNavigationComponent {

    @Composable
    fun ConstructorSeasonScreen(
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass,
        paddingValues: PaddingValues,
        constructorId: String,
        constructorName: String,
        season: Int,
        driverClicked: (driverId: String, driverName: String, season: Int) -> Unit,
    )

    @Composable
    fun ConstructorScreen(
        paddingValues: PaddingValues,
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass,
        constructorId: String,
        constructorName: String,
    )
}
package tmg.flashback.constructors.contract

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable

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
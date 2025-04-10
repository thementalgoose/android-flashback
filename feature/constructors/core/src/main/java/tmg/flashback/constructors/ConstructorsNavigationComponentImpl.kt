package tmg.flashback.constructors

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import tmg.flashback.constructors.contract.ConstructorsNavigationComponent
import tmg.flashback.constructors.presentation.overview.ConstructorOverviewScreenVM
import tmg.flashback.constructors.presentation.season.ConstructorSeasonScreenVM
import javax.inject.Inject

class ConstructorsNavigationComponentImpl @Inject constructor(): ConstructorsNavigationComponent {

    @Composable
    override fun ConstructorSeasonScreen(
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass,
        constructorId: String,
        constructorName: String,
        season: Int,
        driverClicked: (driverId: String, driverName: String, season: Int) -> Unit
    ) {
        ConstructorSeasonScreenVM(
            actionUpClicked = actionUpClicked,
            windowSizeClass = windowSizeClass,
            constructorId = constructorId,
            constructorName = constructorName,
            season = season,
        )
    }

    @Composable
    override fun ConstructorScreen(
        paddingValues: PaddingValues,
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass,
        constructorId: String,
        constructorName: String
    ) {
        ConstructorOverviewScreenVM(
            paddingValues = paddingValues,
            actionUpClicked = actionUpClicked,
            windowSizeClass = windowSizeClass,
            constructorId = constructorId,
            constructorName = constructorName,
        )
    }
}
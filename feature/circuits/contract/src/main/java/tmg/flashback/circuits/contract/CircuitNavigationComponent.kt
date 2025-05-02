package tmg.flashback.circuits.contract

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable

interface CircuitNavigationComponent {

    @Composable
    fun CircuitScreen(
        paddingValues: PaddingValues,
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass,
        circuitId: String,
        circuitName: String
    )
}
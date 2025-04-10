package tmg.flashback.circuits

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import tmg.flashback.circuits.contract.CircuitNavigationComponent
import tmg.flashback.circuits.presentation.CircuitScreenVM
import javax.inject.Inject

class CircuitNavigationComponentImpl @Inject constructor(): CircuitNavigationComponent {

    @Composable
    override fun CircuitScreen(
        paddingValues: PaddingValues,
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass,
        circuitId: String,
        circuitName: String
    ) {
        CircuitScreenVM(
            paddingValues = paddingValues,
            actionUpClicked = actionUpClicked,
            windowSizeClass = windowSizeClass,
            circuitId = circuitId,
            circuitName = circuitName
        )
    }
}
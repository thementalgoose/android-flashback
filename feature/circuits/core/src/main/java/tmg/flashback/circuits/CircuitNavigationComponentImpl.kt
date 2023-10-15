package tmg.flashback.circuits

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import tmg.flashback.circuits.contract.CircuitNavigationComponent
import tmg.flashback.circuits.ui.CircuitScreenVM
import javax.inject.Inject

class CircuitNavigationComponentImpl @Inject constructor(): CircuitNavigationComponent {

    @Composable
    override fun CircuitScreen(
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass,
        circuitId: String,
        circuitName: String
    ) {
        CircuitScreenVM(
            actionUpClicked = actionUpClicked,
            windowSizeClass = windowSizeClass,
            circuitId = circuitId,
            circuitName = circuitName
        )
    }
}
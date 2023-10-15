package tmg.flashback.circuits.contract

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface CircuitUiComponent {
    fun circuitNav(): CircuitNavigationComponent
}

private lateinit var uiComponent: CircuitUiComponent
@Composable
fun requireCircuitNavigationComponent(): CircuitNavigationComponent {
    if (!::uiComponent.isInitialized) {
        uiComponent = EntryPoints.get(LocalContext.current.applicationContext, CircuitUiComponent::class.java)
    }
    return uiComponent.circuitNav()
}
package tmg.flashback.drivers.contract

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface DriverUiComponent {
    fun driversNav(): DriverNavigationComponent
}

private lateinit var uiComponent: DriverUiComponent
@Composable
fun requireDriverNavigationComponent(): DriverNavigationComponent {
    if (!::uiComponent.isInitialized) {
        uiComponent = EntryPoints.get(LocalContext.current.applicationContext, DriverUiComponent::class.java)
    }
    return uiComponent.driversNav()
}
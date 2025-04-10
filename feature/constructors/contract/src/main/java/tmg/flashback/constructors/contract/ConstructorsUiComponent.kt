package tmg.flashback.constructors.contract

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface ConstructorsUiComponent {
    fun constructorsNav(): ConstructorsNavigationComponent
}

private lateinit var uiComponent: ConstructorsUiComponent
@Composable
fun requireConstructorsNavigationComponent(): ConstructorsNavigationComponent {
    if (!::uiComponent.isInitialized) {
        uiComponent = EntryPoints.get(LocalContext.current.applicationContext, ConstructorsUiComponent::class.java)
    }
    return uiComponent.constructorsNav()
}
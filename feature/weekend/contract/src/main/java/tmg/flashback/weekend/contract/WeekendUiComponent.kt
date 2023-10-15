package tmg.flashback.weekend.contract

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface WeekendUiComponent {
    fun weekendNav(): WeekendNavigationComponent
}

private lateinit var uiComponent: WeekendUiComponent
@Composable
fun requireWeekendNavigationComponent(): WeekendNavigationComponent {
    if (!::uiComponent.isInitialized) {
        uiComponent = EntryPoints.get(LocalContext.current.applicationContext, WeekendUiComponent::class.java)
    }
    return uiComponent.weekendNav()
}
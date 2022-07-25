package tmg.flashback.ui.components.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import org.koin.androidx.compose.inject
import tmg.flashback.analytics.manager.AnalyticsManager

@Composable
fun ScreenView(
    screenName: String,
    args: Map<String, String> = mapOf()
) {
//    val analyticsManager: AnalyticsManager by inject()
    DisposableEffect(key1 = Unit, effect = {
        println("SCREEN VIEW $screenName")
//        analyticsManager.viewScreen(screenName, args)
        this.onDispose {
            println("SCREEN Navigate up $screenName")
//            analyticsManager.logEvent("")
        }
    })
}
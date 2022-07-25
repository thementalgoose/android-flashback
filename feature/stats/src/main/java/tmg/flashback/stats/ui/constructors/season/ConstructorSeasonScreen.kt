package tmg.flashback.stats.ui.constructors.season

import androidx.compose.runtime.Composable
import tmg.flashback.stats.analytics.AnalyticsConstants.analyticsConstructorId
import tmg.flashback.stats.analytics.AnalyticsConstants.analyticsSeason
import tmg.flashback.ui.components.analytics.ScreenView

@Composable
fun ConstructorSeasonScreenVM(
    constructorId: String,
    constructorName: String,
    season: Int
) {
    ScreenView(screenName = "Constructor Season", args = mapOf(
        analyticsConstructorId to constructorId,
        analyticsSeason to season.toString()
    ))
}

@Composable
fun ConstructorSeasonScreen() {

}
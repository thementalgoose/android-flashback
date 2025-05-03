package tmg.flashback.navigation

import androidx.navigation.NavHostController
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor(
    private val crashlyticsManager: CrashlyticsManager,
    private val internalNavigationComponent: InternalNavigationComponent,
) {

    lateinit var navController: NavHostController

    fun navigate(screen: Screen) {
        val destination = internalNavigationComponent.getNavigationData(screen)
        crashlyticsManager.log("Navigating to ${destination.route}")
        navController.navigateTo(destination)
    }
}
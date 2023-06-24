package tmg.flashback.navigation

import androidx.navigation.NavHostController
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor(
    private val crashlyticsManager: CrashlyticsManager
) {

    lateinit var navController: NavHostController

    fun navigate(destination: NavigationDestination) {
        crashlyticsManager.log("Navigating to ${destination.route}")
        navController.navigate(destination)
    }
}
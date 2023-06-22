package tmg.flashback.navigation

import androidx.navigation.NavHostController
import tmg.flashback.crashlytics.manager.CrashManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor(
    private val crashManager: CrashManager
) {

    lateinit var navController: NavHostController

    fun navigate(destination: NavigationDestination) {
        crashManager.log("Navigating to ${destination.route}")
        navController.navigate(destination)
    }
}
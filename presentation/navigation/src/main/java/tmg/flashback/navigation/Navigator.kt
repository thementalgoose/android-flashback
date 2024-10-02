package tmg.flashback.navigation

import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
        navController.navigateTo(destination)
    }
}
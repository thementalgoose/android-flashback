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

    private var _subNavigation: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val subNavigation: StateFlow<Boolean> = _subNavigation

    fun navigate(destination: NavigationDestination) {
        crashlyticsManager.log("Navigating to ${destination.route}")
        _subNavigation.value = false
        navController.navigate(destination)
    }

    fun clearSubNavigation() {
        _subNavigation.value = false
    }
    fun isSubNavigation() = subNavigation.value

    fun setSubNavigation() {
        _subNavigation.value = true
    }
}
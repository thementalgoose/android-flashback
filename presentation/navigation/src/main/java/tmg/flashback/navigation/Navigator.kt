package tmg.flashback.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import tmg.flashback.crash_reporting.manager.CrashManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor(
    private val crashManager: CrashManager
) {

    var destination: MutableStateFlow<NavigationDestination?> = MutableStateFlow(null)

    fun navigate(destination: NavigationDestination) {
        crashManager.log("Navigating to ${destination.route}")
        if (this.destination.value != destination) {
            this.destination.value = destination
        }
    }
}
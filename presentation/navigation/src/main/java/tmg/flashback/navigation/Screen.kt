package tmg.flashback.navigation

// Extend this in feature modules
object Screen {
    object Settings

    val AboutThisApp
        get() = NavigationDestination("aboutThisApp", launchSingleTop = true)
}
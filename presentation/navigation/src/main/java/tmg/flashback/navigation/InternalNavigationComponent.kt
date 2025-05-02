package tmg.flashback.navigation

interface InternalNavigationComponent {
    fun getNavigationData(screen: Screen): NavigationDestination
}
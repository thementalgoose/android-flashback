package tmg.flashback.stats.di

@Deprecated("This is temporary whilst migrating to Jetpack Compose")
interface StatsNavigator {
    fun goToTyreOverview(
        season: Int
    )
    fun goToNotificationOnboarding()
}
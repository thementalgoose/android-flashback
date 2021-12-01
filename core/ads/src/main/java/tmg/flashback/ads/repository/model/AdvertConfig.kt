package tmg.flashback.ads.repository.model

data class AdvertConfig(
    val onHomeScreen: Boolean = true,
    val onRaceScreen: Boolean = true,
    val onDriverOverview: Boolean = true,
    val onConstructorOverview: Boolean = true,
    val onSearch: Boolean = true,
    val onRss: Boolean = true,
    val allowUserConfig: Boolean = false,
) {
    val isEnabled: Boolean by lazy {
        return@lazy onHomeScreen || onRaceScreen || onDriverOverview || onConstructorOverview || onSearch || onRss
    }
}
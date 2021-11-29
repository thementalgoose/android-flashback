package tmg.flashback.ads.repository.model

data class AdvertConfig(
    val onHomeScreen: Boolean,
    val onRaceScreen: Boolean,
    val onDriverOverview: Boolean,
    val onConstructorOverview: Boolean,
    val onSearch: Boolean,
    val allowUserConfig: Boolean,
) {
    val isEnabled: Boolean by lazy {
        return@lazy onHomeScreen || onRaceScreen || onDriverOverview || onConstructorOverview || onSearch
    }
}
package tmg.flashback.ads.ads.repository.model

data class AdvertConfig(
    val onHomeScreen: Boolean = true,
    @Deprecated("No longer used")
    val onRaceScreen: Boolean = true,
    val onSearch: Boolean = true,
    val onRss: Boolean = true,
    val allowUserConfig: Boolean = false,
) {
    val isEnabled: Boolean by lazy {
        return@lazy onHomeScreen || onRaceScreen || onSearch || onRss
    }
}
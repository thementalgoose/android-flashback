package tmg.flashback.ads.repository.converters

import tmg.flashback.ads.repository.json.AdvertConfigJson
import tmg.flashback.ads.repository.model.AdvertConfig

fun AdvertConfigJson?.convert(): AdvertConfig {
    return AdvertConfig(
        onHomeScreen = this?.locations?.home ?: false,
        onRaceScreen = this?.locations?.race ?: false,
        onDriverOverview = this?.locations?.driverOverview ?: false,
        onConstructorOverview = this?.locations?.constructorOverview ?: false,
        onSearch = this?.locations?.search ?: false,
        onRss = this?.locations?.rss ?: false,
        allowUserConfig = this?.allowUserConfig ?: false,
    )
}
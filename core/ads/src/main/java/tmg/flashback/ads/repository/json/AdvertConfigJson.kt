package tmg.flashback.ads.repository.json

import kotlinx.serialization.Serializable

@Serializable
data class AdvertConfigJson(
    val locations: AdvertLocationJson? = null,
    val allowUserConfig: Boolean? = null
)

@Serializable
data class AdvertLocationJson(
    val home: Boolean? = null,
    val race: Boolean? = null,
    val rss: Boolean? = null,
    val driverOverview: Boolean? = null,
    val constructorOverview: Boolean? = null,
    val search: Boolean? = null
)
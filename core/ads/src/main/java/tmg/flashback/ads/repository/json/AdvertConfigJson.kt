package tmg.flashback.ads.repository.json

data class AdvertConfigJson(
    val locations: AdvertLocationJson? = null,
    val allowUserConfig: Boolean? = null
)

data class AdvertLocationJson(
    val home: Boolean? = null,
    val race: Boolean? = null,
    val driverOverview: Boolean? = null,
    val constructorOverview: Boolean? = null,
    val search: Boolean? = null
)
package tmg.flashback.stats.repository.converters

import tmg.flashback.stats.repository.json.BannerItemJson
import tmg.flashback.stats.repository.json.BannerJson
import tmg.flashback.stats.repository.models.Banner

fun BannerJson.convert(): List<Banner> {
    return (this.banners ?: emptyList())
        .mapNotNull { it.convert() }
}

fun BannerItemJson.convert(): Banner? {
    if (this.msg.isNullOrEmpty()) {
        return null
    }
    return Banner(
        message = this.msg,
        url = this.url.takeIf { it?.isNotBlank() == true },
        highlight = this.highlight ?: false,
        season = this.season
    )
}
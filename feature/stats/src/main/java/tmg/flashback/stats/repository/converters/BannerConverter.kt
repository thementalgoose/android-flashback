package tmg.flashback.stats.repository.converters

import tmg.flashback.stats.repository.json.BannerJson
import tmg.flashback.stats.repository.models.Banner

fun BannerJson.convert(): Banner? {
    if (this.msg.isNullOrEmpty()) {
        return null
    }
    return Banner(
        message = this.msg,
        url = this.url
    )
}
package tmg.flashback.statistics.repository.converters

import tmg.flashback.statistics.repository.json.BannerJson
import tmg.flashback.statistics.repository.models.Banner

fun BannerJson.convert(): Banner? {
    if (this.msg.isNullOrEmpty()) {
        return null
    }
    return Banner(
        message = this.msg,
        url = this.url
    )
}
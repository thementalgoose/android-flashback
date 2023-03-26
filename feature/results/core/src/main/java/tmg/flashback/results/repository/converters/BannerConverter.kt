package tmg.flashback.results.repository.converters

import android.webkit.URLUtil
import tmg.flashback.crash_reporting.manager.CrashManager
import tmg.flashback.results.BuildConfig
import tmg.flashback.results.repository.json.BannerItemJson
import tmg.flashback.results.repository.json.BannerJson
import tmg.flashback.results.repository.models.Banner
import java.text.ParseException

fun BannerJson.convert(
    crashManager: CrashManager
): List<Banner> {
    return (this.banners ?: emptyList())
        .mapNotNull { it.convert(crashManager) }
}

fun BannerItemJson.convert(
    crashManager: CrashManager
): Banner? {
    if (this.msg.isNullOrEmpty()) {
        return null
    }
    return Banner(
        message = this.msg,
        url = try {
            when {
                this.url == null -> null
                URLUtil.isValidUrl(this.url) -> this.url
                else -> {
                    crashManager.logException(ParseException("Banner url ${this.url} is not a valid URL", 0))
                    null
                }
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            crashManager.logException(e)
            null
        },
        highlight = this.highlight ?: false,
        season = this.season
    )
}
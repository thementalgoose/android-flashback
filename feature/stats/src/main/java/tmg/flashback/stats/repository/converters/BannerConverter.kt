package tmg.flashback.stats.repository.converters

import android.webkit.URLUtil
import tmg.flashback.crash_reporting.manager.CrashManager
import tmg.flashback.stats.BuildConfig
import tmg.flashback.stats.repository.json.BannerItemJson
import tmg.flashback.stats.repository.json.BannerJson
import tmg.flashback.stats.repository.models.Banner

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
            when (URLUtil.isValidUrl(this.url)) {
                true -> this.url
                false -> null
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
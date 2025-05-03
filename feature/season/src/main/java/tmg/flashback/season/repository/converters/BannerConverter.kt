package tmg.flashback.season.repository.converters

import android.webkit.URLUtil
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.season.BuildConfig
import tmg.flashback.season.repository.json.BannerItemJson
import tmg.flashback.season.repository.json.BannerJson
import tmg.flashback.season.repository.models.Banner
import java.text.ParseException

fun BannerJson.convert(
    crashlyticsManager: CrashlyticsManager
): List<Banner> {
    return (this.banners ?: emptyList())
        .mapNotNull { it.convert(crashlyticsManager) }
}

fun BannerItemJson.convert(
    crashlyticsManager: CrashlyticsManager
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
                    crashlyticsManager.logException(ParseException("Banner url ${this.url} is not a valid URL", 0))
                    null
                }
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            crashlyticsManager.logException(e)
            null
        },
        highlight = this.highlight ?: false,
        season = this.season
    )
}
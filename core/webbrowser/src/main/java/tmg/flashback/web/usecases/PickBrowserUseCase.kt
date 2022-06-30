package tmg.flashback.web.usecases

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.webkit.URLUtil
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.web.repository.WebBrowserRepository
import tmg.flashback.web.ui.browser.WebActivity

class PickBrowserUseCase(
    private val webBrowserRepository: WebBrowserRepository,
    private val analyticsManager: AnalyticsManager
) {
    fun open(activity: Activity, url: String, title: String) {
        analyticsManager.logEvent("Opening URL", mapOf(
            "url" to url,
            "title" to title
        ))
        when {
            isMaps(url) && isLocationIntentAvailable(activity) -> {
                activity.startActivity(openMaps(url))
            }
            isYoutube(url) && URLUtil.isValidUrl(url) -> {
                activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
            isPlayStore(url) && URLUtil.isValidUrl(url) -> {
                activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
            webBrowserRepository.openInExternal && URLUtil.isValidUrl(url) -> {
                activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
            else -> {
                activity.startActivity(WebActivity.intent(context = activity, url = url, title = title))
            }
        }
    }

    private fun openMaps(url: String): Intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

    private fun isMaps(url: String): Boolean {
        return url.startsWith("geo:")
    }
    private fun isPlayStore(url: String): Boolean {
        return url.startsWith("https://play.google.com")
    }
    private fun isYoutube(url: String): Boolean {
        return url.startsWith("https://youtube.com") || url.startsWith("https://www.youtube.com")
    }

    private fun isLocationIntentAvailable(context: Context): Boolean {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:32.5558485,34.65522447"))
        return context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isNotEmpty()
    }
}
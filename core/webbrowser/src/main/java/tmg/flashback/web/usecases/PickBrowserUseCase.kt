package tmg.flashback.web.usecases

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.webkit.URLUtil
import tmg.flashback.web.repository.WebBrowserRepository
import tmg.flashback.web.ui.browser.WebActivity

class PickBrowserUseCase(
    private val webBrowserRepository: WebBrowserRepository
) {
    fun open(activity: Activity, url: String, title: String) {
        if (!URLUtil.isValidUrl(url)) { return }

        when {
            isLocationIntentAvailable(activity) -> {
                activity.startActivity(openMaps(url))
            }
            webBrowserRepository.openInExternal ||
            isPlayStore(url) -> {
                activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
            else -> {
                activity.startActivity(WebActivity.intent(context = activity, url = url, title = title))
            }
        }
    }

    private fun openMaps(url: String): Intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

    private fun isPlayStore(url: String): Boolean {
        return url.startsWith("https://play.google.com")
    }

    private fun isLocationIntentAvailable(context: Context): Boolean {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:32.5558485,34.65522447"))
        return context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isNotEmpty()
    }
}
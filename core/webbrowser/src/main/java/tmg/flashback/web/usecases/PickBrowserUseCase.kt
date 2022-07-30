package tmg.flashback.web.usecases

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.webkit.URLUtil
import android.widget.Toast
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.web.R
import tmg.flashback.web.repository.WebBrowserRepository
import tmg.flashback.web.ui.browser.WebActivity
import tmg.utilities.extensions.managerClipboard
import tmg.utilities.utils.ClipboardUtils.Companion.copyToClipboard

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
                tryOpen(activity, url) {
                    activity.startActivity(openMaps(url))
                }
            }
            isYoutube(url) && URLUtil.isValidUrl(url) -> {
                tryOpen(activity, url) {
                    activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            }
            isPlayStore(url) && URLUtil.isValidUrl(url) -> {
                tryOpen(activity, url) {
                    activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            }
            webBrowserRepository.openInExternal && URLUtil.isValidUrl(url) -> {
                tryOpen(activity, url) {
                    activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            }
            else -> {
                activity.startActivity(
                    WebActivity.intent(
                        context = activity,
                        url = url,
                        title = title
                    )
                )
            }
        }
    }

    private fun tryOpen(activity: Activity, url: String, content: () -> Unit) {
        try {
            content()
        } catch (e: ActivityNotFoundException) {
            val clipboard = copyToClipboard(activity, url)
            if (!clipboard) {
                Toast.makeText(
                    activity,
                    activity.getString(R.string.unable_to_open_url),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun copyToClipboard(activity: Activity, url: String): Boolean {
        val clipboardManager = activity.managerClipboard ?: return false
        clipboardManager.setPrimaryClip(ClipData.newPlainText("", url))
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            Toast.makeText(activity, R.string.copy_to_clipboard, Toast.LENGTH_LONG)
                .show()
        }
        return true
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
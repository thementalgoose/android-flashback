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
import tmg.flashback.googleanalytics.manager.FirebaseAnalyticsManager
import tmg.flashback.navigation.ActivityProvider
import tmg.flashback.web.R
import tmg.flashback.web.repository.WebBrowserRepository
import tmg.flashback.web.ui.browser.WebActivity
import tmg.utilities.extensions.managerClipboard
import javax.inject.Inject

class OpenWebpageUseCase @Inject constructor(
    private val webBrowserRepository: WebBrowserRepository,
    private val firebaseAnalyticsManager: FirebaseAnalyticsManager,
    private val activityProvider: ActivityProvider,
) {
    fun open(url: String, title: String, forceExternal: Boolean = false) {
        activityProvider.launch {
            open(activity = it, url = url, title = title, forceExternal = forceExternal)
        }
    }

    fun open(activity: Activity, url: String, title: String, forceExternal: Boolean = false) {
        firebaseAnalyticsManager.logEvent("Opening URL", mapOf(
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
            forceExternal || (webBrowserRepository.openInExternal && URLUtil.isValidUrl(url)) -> {
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
        return listOf(
            context.isPackageInstalled("com.google.android.apps.maps"),
            context.isPackageInstalled("com.waze")
        ).any()
    }

    private fun Context.isPackageInstalled(packageName: String): Boolean = try {
        val packageInfo = this.packageManager.getApplicationInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    } catch (e: java.lang.RuntimeException) {
        false
    }
}
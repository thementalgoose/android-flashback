package tmg.flashback.web.usecases

import android.app.Activity
import android.webkit.URLUtil
import tmg.flashback.device.usecases.OpenUrlUseCase
import tmg.flashback.googleanalytics.manager.FirebaseAnalyticsManager
import tmg.flashback.device.ActivityProvider
import tmg.flashback.web.repository.WebBrowserRepository
import tmg.flashback.web.presentation.browser.WebActivity
import javax.inject.Inject

class OpenWebpageUseCase @Inject constructor(
    private val webBrowserRepository: WebBrowserRepository,
    private val firebaseAnalyticsManager: FirebaseAnalyticsManager,
    private val activityProvider: ActivityProvider,
    private val openUrlUseCase: OpenUrlUseCase
) {

    fun open(url: String, title: String, forceExternal: Boolean = false) {
        activityProvider.launch {
            open(activity = it, url = url, title = title, forceExternal = forceExternal)
        }
    }

    fun open(activity: Activity, url: String, title: String, forceExternal: Boolean = false) {
        firebaseAnalyticsManager.logEvent("Opening URL", mapOf("url" to url, "title" to title))
        when {
            url.isPlayStore() || url.isYoutube() -> {
                openUrlUseCase.openUrl(url)
            }
            forceExternal || (webBrowserRepository.openInExternal && URLUtil.isValidUrl(url)) -> {
                openUrlUseCase.openUrl(url)
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

    companion object {
        private fun String.isPlayStore(): Boolean {
            return this.startsWith("https://play.google.com")
        }
        private fun String.isYoutube(): Boolean {
            return this.startsWith("https://youtube.com") || this.startsWith("https://www.youtube.com") ||
                    this.startsWith("https://youtu.be") || this.startsWith(("https://www.youtu.be"))
        }
    }
}
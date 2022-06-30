package tmg.flashback.web

import android.content.Context
import android.content.Intent
import tmg.flashback.ui.navigation.ActivityProvider
import tmg.flashback.web.ui.browser.WebActivity
import tmg.flashback.web.ui.settings.SettingsWebBrowserActivity

class WebNavigationComponent(
    private val activityProvider: ActivityProvider
) {
    internal fun webIntent(context: Context, url: String, title: String = ""): Intent {
        return WebActivity.intent(context, url, title)
    }

    fun web(url: String, title: String = "") = activityProvider.launch {
        val intent = webIntent(it, url, title)
        it.startActivity(intent)
    }

    internal fun webSettingsIntent(context: Context): Intent {
        return SettingsWebBrowserActivity.intent(context)
    }

    fun webSettings() = activityProvider.launch {
        val intent = webSettingsIntent(it)
        it.startActivity(intent)
    }
}
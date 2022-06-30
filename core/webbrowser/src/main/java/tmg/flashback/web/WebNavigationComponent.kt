package tmg.flashback.web

import android.content.Context
import android.content.Intent
import tmg.flashback.ui.navigation.ActivityProvider
import tmg.flashback.web.ui.browser.WebActivity
import tmg.flashback.web.ui.settings.SettingsWebBrowserActivity
import tmg.flashback.web.usecases.PickBrowserUseCase

class WebNavigationComponent(
    private val activityProvider: ActivityProvider,
    private val pickBrowserUseCase: PickBrowserUseCase
) {
    fun web(url: String, title: String = "") = activityProvider.launch {
        pickBrowserUseCase.open(it, url, title)
    }

    internal fun webSettingsIntent(context: Context): Intent {
        return SettingsWebBrowserActivity.intent(context)
    }

    fun webSettings() = activityProvider.launch {
        val intent = webSettingsIntent(it)
        it.startActivity(intent)
    }
}
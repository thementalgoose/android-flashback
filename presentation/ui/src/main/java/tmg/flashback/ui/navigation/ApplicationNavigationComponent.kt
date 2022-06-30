package tmg.flashback.ui.navigation

import android.content.Context
import android.content.Intent

interface ApplicationNavigationComponent {

    fun relaunchApp()
    fun relaunchAppIntent(context: Context): Intent

    fun aboutApp()
    fun aboutAppIntent(context: Context): Intent

    fun syncActivity()
    fun syncActivityIntent(context: Context): Intent

    fun settings()
    fun settingsIntent(context: Context): Intent

    fun openUrl(url: String)

//    fun openInAppBrowser(url: String, title: String? = null)
//    fun openInExternalBrowser(url: String)
}
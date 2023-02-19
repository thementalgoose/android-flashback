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
}
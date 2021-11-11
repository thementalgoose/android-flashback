package tmg.flashback.ui.navigation

import android.content.Context
import android.content.Intent

interface NavigationProvider {
    fun relaunchAppIntent(context: Context): Intent
    fun aboutAppIntent(context: Context): Intent

    fun syncActivityIntent(context: Context): Intent
}
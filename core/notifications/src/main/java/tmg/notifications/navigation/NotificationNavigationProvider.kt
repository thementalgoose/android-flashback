package tmg.notifications.navigation

import android.content.Context
import android.content.Intent

interface NotificationNavigationProvider {
    fun relaunchAppIntent(context: Context): Intent
}
package tmg.flashback.navigation

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

interface ApplicationNavigationComponent {

    fun relaunchApp()
    fun relaunchAppIntent(context: Context): Intent

    fun aboutApp()
    fun aboutAppIntent(context: Context): Intent

    fun syncActivity()
    fun syncActivityIntent(context: Context): Intent

    fun settings()



    fun appSettingsNotifications()

    @RequiresApi(Build.VERSION_CODES.O)
    fun appSettingsNotificationsIntent(): Intent

    @RequiresApi(Build.VERSION_CODES.S)
    fun appSettingsSpecialPermissions()

    @RequiresApi(Build.VERSION_CODES.S)
    fun appSettingsSpecialPermissionsIntent(): Intent
}
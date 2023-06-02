package tmg.flashback.widgets

import android.app.Activity
import android.content.Context
import android.content.Intent

interface WidgetNavigationComponent {
    fun launchApp(context: Context): Intent
    fun getHomeActivityClass(): Class<out Activity>
}
package tmg.flashback.widgets

import android.content.Context
import android.content.Intent

interface WidgetNavigationComponent {
    fun launchApp(context: Context): Intent
}
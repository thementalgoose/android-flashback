package tmg.flashback.widgets.contract

import android.content.Context
import android.content.Intent

interface WidgetNavigationComponent {
    fun getLaunchAppIntent(context: Context): Intent
}
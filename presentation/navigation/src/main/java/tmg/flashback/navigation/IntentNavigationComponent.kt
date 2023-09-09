package tmg.flashback.navigation

import android.content.Intent

interface IntentNavigationComponent {
    fun openIntent(intent: Intent)
}
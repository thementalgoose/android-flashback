package tmg.flashback.managers

import android.content.Intent
import tmg.flashback.device.ActivityProvider
import tmg.flashback.navigation.IntentNavigationComponent
import javax.inject.Inject

class AppIntentNavigationComponent @Inject constructor(
    private val topActivityProvider: ActivityProvider
): IntentNavigationComponent {
    override fun openIntent(intent: Intent) {
        topActivityProvider.activity?.startActivity(intent)
    }
}
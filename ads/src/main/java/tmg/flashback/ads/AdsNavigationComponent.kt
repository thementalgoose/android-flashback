package tmg.flashback.ads

import android.content.Context
import android.content.Intent
import tmg.flashback.ads.ui.settings.adverts.SettingsAdvertActivity
import tmg.flashback.ui.navigation.ActivityProvider

class AdsNavigationComponent(
    private val activityProvider: ActivityProvider
) {
    internal fun settingsAdsIntent(context: Context): Intent {
        return Intent(context, SettingsAdvertActivity::class.java)
    }

    fun settingsAds() = activityProvider.launch {
        it.startActivity(settingsAdsIntent(it))
    }
}
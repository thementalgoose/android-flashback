package tmg.flashback.di.toggle

import android.content.Context
import android.os.Build
import tmg.flashback.R
import tmg.flashback.repo.toggle.ToggleDB

class FlashbackToggleDB(
    private val applicationContext: Context
): ToggleDB {

    override val isRSSEnabled: Boolean
        get() = applicationContext.resources.getBoolean(R.bool.toggle_rss)

    override val isNotificationChannelsSupported: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
}
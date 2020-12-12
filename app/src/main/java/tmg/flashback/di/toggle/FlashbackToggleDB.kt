package tmg.flashback.di.toggle

import android.content.Context
import android.os.Build
import tmg.flashback.R
import tmg.flashback.repo.toggle.ToggleRepository

class FlashbackToggleDB(
    private val applicationContext: Context
): ToggleRepository {

    override val isRSSEnabled: Boolean
        get() = applicationContext.resources.getBoolean(R.bool.toggle_rss)

    override val isNotificationChannelsSupported: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
}
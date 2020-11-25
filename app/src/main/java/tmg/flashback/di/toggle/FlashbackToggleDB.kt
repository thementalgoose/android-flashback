package tmg.flashback.di.toggle

import android.content.Context
import tmg.flashback.BuildConfig
import tmg.flashback.R
import tmg.flashback.repo.ToggleDB
import tmg.utilities.extensions.getBoolean

class FlashbackToggleDB(
    private val applicationContext: Context
): ToggleDB {
    override val isRSSEnabled: Boolean
        get() = applicationContext.resources.getBoolean(R.bool.toggle_rss)

}
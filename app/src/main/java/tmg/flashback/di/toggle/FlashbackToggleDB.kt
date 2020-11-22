package tmg.flashback.di.toggle

import tmg.flashback.BuildConfig

class FlashbackToggleDB: ToggleDB {
    override val isRSSEnabled: Boolean
        get() = BuildConfig.RSS

}
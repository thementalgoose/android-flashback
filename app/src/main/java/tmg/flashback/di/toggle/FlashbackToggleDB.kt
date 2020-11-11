package tmg.flashback.di.toggle

import tmg.flashback.BuildConfig

class FlashbackToggleDB: ToggleDB {
    override val isNewsEnabled: Boolean
        get() = BuildConfig.NEWS

}
package tmg.flashback.di.remoteconfig

import tmg.flashback.core.managers.ConfigurationManager

internal object MockRemoteConfigManager: ConfigurationManager {
    override fun setDefaults() {

    }

    override suspend fun update(andActivate: Boolean): Boolean {
        return true
    }

    override suspend fun activate(): Boolean {
        return true
    }
}
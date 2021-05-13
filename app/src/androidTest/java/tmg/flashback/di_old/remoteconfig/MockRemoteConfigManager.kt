package tmg.flashback.di_old.remoteconfig

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
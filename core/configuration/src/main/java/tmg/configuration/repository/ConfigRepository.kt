package tmg.configuration.repository

import tmg.core.prefs.manager.PreferenceManager

class ConfigRepository(
        private val preferenceManager: PreferenceManager
) {

    companion object {
        private const val keyRemoteConfigSync: String = "REMOTE_CONFIG_SYNC_COUNT"
    }

    //region Shared Prefs

    var remoteConfigSync: Int
        get() = preferenceManager.getInt(keyRemoteConfigSync, 0)
        set(value) = preferenceManager.save(keyRemoteConfigSync, value)

    //endregion

}
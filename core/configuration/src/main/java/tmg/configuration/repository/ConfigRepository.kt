package tmg.configuration.repository

import tmg.core.prefs.manager.PreferenceManager

class ConfigRepository(
        private val preferenceManager: PreferenceManager
) {

    companion object {
        private const val keyRemoteConfigSync: String = "REMOTE_CONFIG_SYNC_COUNT"
        private const val keyRemoteConfigResetCalledAtMigrationVersion: String = "REMOTE_CONFIG_RESET_CALL"
    }

    //region Shared Prefs

    var remoteConfigSync: Int
        get() = preferenceManager.getInt(keyRemoteConfigSync, 0)
        set(value) = preferenceManager.save(keyRemoteConfigSync, value)

    var resetAtMigrationVersion: Int
        get() = preferenceManager.getInt(keyRemoteConfigResetCalledAtMigrationVersion, 0)
        set(value) = preferenceManager.save(keyRemoteConfigResetCalledAtMigrationVersion, value)

    //endregion

}
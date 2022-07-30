package tmg.flashback.configuration.repository

import tmg.flashback.configuration.constants.Migrations
import tmg.flashback.prefs.manager.PreferenceManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigRepository @Inject constructor(
        private val preferenceManager: PreferenceManager
) {

    companion object {
        private const val keyRemoteConfigSync: String = "REMOTE_CONFIG_SYNC_COUNT"
        private const val keyRemoteConfigResetCalledAtMigrationVersion: String = "REMOTE_CONFIG_RESET_CALL"
    }

    //region Shared Prefs

    val requireSynchronisation: Boolean
        get() = Migrations.configurationSyncCount != remoteConfigSync

    internal var remoteConfigSync: Int
        get() = preferenceManager.getInt(keyRemoteConfigSync, 0)
        set(value) = preferenceManager.save(keyRemoteConfigSync, value)

    internal var resetAtMigrationVersion: Int
        get() = preferenceManager.getInt(keyRemoteConfigResetCalledAtMigrationVersion, 0)
        set(value) = preferenceManager.save(keyRemoteConfigResetCalledAtMigrationVersion, value)

    //endregion

}
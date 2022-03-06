package tmg.flashback.configuration.usecases

import tmg.flashback.configuration.constants.Migrations
import tmg.flashback.configuration.repository.ConfigRepository
import tmg.flashback.configuration.services.RemoteConfigService

class ResetConfigUseCase(
    private val configService: RemoteConfigService,
    private val configRepository: ConfigRepository
) {
    suspend fun reset(): Boolean {
        configRepository.remoteConfigSync = 0
        return configService.reset()
    }

    suspend fun ensureReset(): Boolean {
        val existing = configRepository.resetAtMigrationVersion
        if (existing != Migrations.configurationSyncCount) {
            configRepository.resetAtMigrationVersion = Migrations.configurationSyncCount
            configService.reset()
        }
        return true
    }
}
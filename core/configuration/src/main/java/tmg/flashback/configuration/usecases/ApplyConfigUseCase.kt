package tmg.flashback.configuration.usecases

import tmg.flashback.configuration.constants.Migrations
import tmg.flashback.configuration.repository.ConfigRepository
import tmg.flashback.configuration.services.RemoteConfigService
import javax.inject.Inject

class ApplyConfigUseCase @Inject constructor(
    private val configService: RemoteConfigService,
    private val configRepository: ConfigRepository
) {
    suspend fun apply(): Boolean {
        val result = configService.activate()
        if (result) {
            configRepository.remoteConfigSync = Migrations.configurationSyncCount
        }
        return result
    }
}
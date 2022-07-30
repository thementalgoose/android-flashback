package tmg.flashback.configuration.usecases

import tmg.flashback.configuration.constants.Migrations
import tmg.flashback.configuration.repository.ConfigRepository
import tmg.flashback.configuration.services.RemoteConfigService
import javax.inject.Inject

class FetchConfigUseCase @Inject constructor(
    private val configService: RemoteConfigService,
    private val configRepository: ConfigRepository
) {
    suspend fun fetch() = fetch(false)

    suspend fun fetchAndApply() = fetch(true)

    private suspend fun fetch(apply: Boolean): Boolean {
        val result = configService.fetch(apply)
        if (result && apply) {
            configRepository.remoteConfigSync = Migrations.configurationSyncCount
        }
        return result
    }
}
package tmg.flashback.configuration.usecases

import tmg.flashback.configuration.services.RemoteConfigService

class InitialiseConfigUseCase(
    private val configService: RemoteConfigService
) {
    fun initialise() {
        configService.initialiseRemoteConfig()
    }
}
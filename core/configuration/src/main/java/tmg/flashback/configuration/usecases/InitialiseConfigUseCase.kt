package tmg.flashback.configuration.usecases

import tmg.flashback.configuration.services.RemoteConfigService
import javax.inject.Inject

class InitialiseConfigUseCase @Inject constructor(
    private val configService: RemoteConfigService
) {
    fun initialise() {
        configService.initialiseRemoteConfig()
    }
}
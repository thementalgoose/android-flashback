package tmg.flashback.managers

import tmg.flashback.BuildConfig
import tmg.flashback.repositories.NetworkConfigRepository
import tmg.flashback.statistics.network.NetworkConfigManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNetworkConfigManager @Inject constructor(
    private val networkConfigRepository: NetworkConfigRepository
): NetworkConfigManager {

    override val baseUrl: String
        get() = networkConfigRepository.configUrl

    override val apiKey: String
        get() = ""

    override val isDebug: Boolean
        get() = BuildConfig.DEBUG

    override val defaultBaseUrl: String
        get() = BuildConfig.BASE_URL
}
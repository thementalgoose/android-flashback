package tmg.flashback.managers

import tmg.flashback.BuildConfig
import tmg.flashback.repositories.NetworkConfigRepository
import tmg.flashback.statistics.network.NetworkConfigManager

class AppNetworkConfigManager: NetworkConfigManager {

    override val baseUrl: String
        get() = BuildConfig.BASE_URL

    override val apiKey: String
        get() = ""

    override val isDebug: Boolean
        get() = BuildConfig.DEBUG
}
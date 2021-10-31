package tmg.flashback.managers

import tmg.flashback.BuildConfig
import tmg.flashback.statistics.network.NetworkConfigManager

class AppNetworkConfigManager: NetworkConfigManager {

    override val baseUrl: String
        get() = "https://f1stats-sand.web.app/"
    override val apiKey: String
        get() = ""
    override val isDebug: Boolean
        get() = BuildConfig.DEBUG

}
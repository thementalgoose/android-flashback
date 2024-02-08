package tmg.flashback.flashbacknews.api

interface ApiNetworkConfigManager {
    val isDebug: Boolean

    val defaultBaseUrl: String
}
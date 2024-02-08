package tmg.flashback.flashbacknews.api

interface NewsNetworkConfigManager {
    val isDebug: Boolean

    val defaultBaseUrl: String
}
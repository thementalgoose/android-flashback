package tmg.flashback.statistics.network

interface NetworkConfigManager {
    val baseUrl: String
    val apiKey: String
    val isDebug: Boolean

    val defaultBaseUrl: String
}
package tmg.flashback.flashbackapi.api

interface NetworkConfigManager {
    val baseUrl: String
    val apiKey: String
    val isDebug: Boolean

    val defaultBaseUrl: String
}
package tmg.flashback.managers.analytics

interface AnalyticsManager {

    var enableAnalytics: Boolean

    fun viewScreen(screenName: String, clazz: Class<*>, mapOfParams: Map<String, String> = emptyMap())
}
package tmg.flashback.managers.analytics

interface AnalyticsManager {

    fun viewScreen(screenName: String, clazz: Class<*>, mapOfParams: Map<String, String> = emptyMap())
}
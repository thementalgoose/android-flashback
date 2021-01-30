package tmg.flashback.rss.managers

interface RSSAnalyticsManager {

    var enableAnalytics: Boolean

    fun rssViewScreen(screenName: String, clazz: Class<*>, mapOfParams: Map<String, String> = emptyMap())
}
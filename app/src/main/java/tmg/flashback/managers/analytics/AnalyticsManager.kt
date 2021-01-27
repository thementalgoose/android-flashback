package tmg.flashback.managers.analytics

interface AnalyticsManager {

    fun setting(setting: String, optIn: Boolean)
    fun click(item: String)
    fun viewScreen(name: String)

}
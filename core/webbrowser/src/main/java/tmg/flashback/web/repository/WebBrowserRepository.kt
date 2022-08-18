package tmg.flashback.web.repository

import tmg.flashback.prefs.manager.PreferenceManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebBrowserRepository @Inject constructor(
    private val preferenceManager: PreferenceManager
) {

    internal var openInExternal: Boolean
        get() = preferenceManager.getBoolean(keyOpenInExternal, true)
        set(value) {
            preferenceManager.save(keyOpenInExternal, value)
        }

    internal var enableJavascript: Boolean
        get() = preferenceManager.getBoolean(keyEnableJavascript, true)
        set(value) {
            preferenceManager.save(keyEnableJavascript, value)
        }

    companion object {
        private const val keyOpenInExternal = "WEB_BROWSER_OPEN_IN_EXTERNAL"
        private const val keyEnableJavascript = "WEB_BROWSER_ENABLE_JAVASCRIPT"
    }
}
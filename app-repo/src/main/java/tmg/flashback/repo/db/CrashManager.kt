package tmg.flashback.repo.db

import java.lang.Exception

interface CrashManager {

    var enableCrashReporting: Boolean

    fun initialise(appOpenedCount: Int? = null, appFirstOpened: String? = null)
    fun log(msg: String)
    fun logError(error: Exception, context: String)
}
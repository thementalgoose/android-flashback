package tmg.flashback.repo.db

import java.lang.Exception

interface CrashReporter {

    val isDebug: Boolean

    fun initialise()
    fun log(msg: String)
    fun logError(error: Exception, context: String)
}
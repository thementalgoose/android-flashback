package tmg.f1stats.repo.db

import java.lang.Exception

interface CrashReporter {
    fun initialise()
    fun log(msg: String)
    fun logError(error: Exception, context: String)
}
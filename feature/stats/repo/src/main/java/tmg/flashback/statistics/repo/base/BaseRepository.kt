package tmg.flashback.statistics.repo.base

import tmg.crash_reporting.controllers.CrashController
import java.lang.RuntimeException

abstract class BaseRepository(
    val crashController: CrashController
) {
//    fun <T> attempt(msgIfFailed: String? = null, closure: () -> T?): T? {
//        return try {
//            closure.invoke()
//        } catch (e: RuntimeException) {
//            crashController.logException(e, msgIfFailed)
//            null
//        }
//    }

    suspend fun attempt(msgIfFailed: String? = null, closure: suspend () -> Boolean): Boolean {
        return try {
            val result = closure.invoke()
            result
        } catch (e: RuntimeException) {
            crashController.logException(e, msgIfFailed)
            false
        }
    }
}
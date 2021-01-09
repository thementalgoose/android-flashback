package tmg.flashback.firebase.base

import tmg.flashback.firebase.crash.FirebaseCrashManager
import java.time.LocalDate

/**
 * Base converter class to provide
 */
class BaseConverter(
        private val isDebug: Boolean,
        private val crashManager: FirebaseCrashManager?
) {
    protected fun <T,O> given(model: T?, block: GivenBlock<T,O>.() -> Unit): O? {
        if (model == null) {
            return null
        }
        val blockRunner = GivenBlock<T,O>(model, isDebug, crashManager)
        block(blockRunner)
        return blockRunner.result
    }
}
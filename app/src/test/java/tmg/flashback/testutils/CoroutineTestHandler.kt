package tmg.flashback.testutils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

// https://medium.com/swlh/kotlin-coroutines-in-android-unit-test-28ff280fc0d5
@ExperimentalCoroutinesApi
class CoroutineTestHandler: BeforeEachCallback, AfterEachCallback {

    private val coroutineDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = TestCoroutineScope(coroutineDispatcher)

    override fun beforeEach(context: ExtensionContext?) {
        Dispatchers.setMain(coroutineDispatcher)
    }

    override fun afterEach(context: ExtensionContext?) {
        Dispatchers.resetMain()
        coroutineScope.cleanupTestCoroutines()
    }
}
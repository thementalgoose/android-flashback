package tmg.flashback.rss.testutils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runners.model.Statement

@ExperimentalCoroutinesApi
class CoroutineRule : TestRule {

    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
    val testScope: TestCoroutineScope = TestCoroutineScope(testDispatcher)

    override fun apply(base: Statement?, description: Description?) = object : Statement() {
        override fun evaluate() {
            Dispatchers.setMain(testDispatcher)

            base?.evaluate()

            Dispatchers.resetMain()
            testScope.cleanupTestCoroutines()
            testDispatcher.cleanupTestCoroutines()
        }
    }
}
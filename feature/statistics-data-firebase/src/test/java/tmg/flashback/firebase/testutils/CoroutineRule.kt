package tmg.flashback.firebase.testutils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

internal class CoroutineRule : TestRule {

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
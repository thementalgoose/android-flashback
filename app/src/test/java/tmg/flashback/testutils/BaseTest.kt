package tmg.flashback.testutils

import androidx.annotation.CallSuper
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.*

@ExtendWith(TestingTaskExecutor::class)
open class BaseTest {

    @get:Rule
    val coroutineScope = CoroutineRule()

    private val testDispatcher = coroutineScope.testDispatcher
    private val testScope = coroutineScope.testScope

    val testScopeProvider = TestScopeProvider(testScope)

    @BeforeEach
    @CallSuper
    open fun beforeAll() {
        Dispatchers.setMain(testDispatcher)
    }

    /**
     * Run a test with test coroutine scope
     * - advanceUntilIdle()
     */
    fun coroutineTest(block: TestCoroutineScope.() -> Unit) {
        runBlockingTest(testDispatcher) {
            block(this)
        }
    }
}

/**
 * Task executor to set threads to using main
 */
private class TestingTaskExecutor: BeforeEachCallback, AfterEachCallback {
    override fun beforeEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(TestTaskExecutor)
    }

    override fun afterEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }
}

object TestTaskExecutor: TaskExecutor() {
    override fun executeOnDiskIO(runnable: Runnable) = runnable.run()
    override fun isMainThread(): Boolean = true
    override fun postToMainThread(runnable: Runnable) = runnable.run()
}
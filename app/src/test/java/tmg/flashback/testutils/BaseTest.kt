package tmg.flashback.testutils

import androidx.annotation.CallSuper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Rule
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext

// https://stackoverflow.com/questions/62332403/how-to-inject-viewmodelscope-for-android-unit-test-with-kotlin-coroutines

@ExperimentalCoroutinesApi
@ExtendWith(TestingTaskExecutor::class)
open class BaseTest {


    @get:Rule
    val coroutineScope = CoroutineRule()

    val testDispatcher = TestCoroutineDispatcher()
    val testScope = TestCoroutineScope(testDispatcher)

    @BeforeEach
    @CallSuper
    private fun beforeAll() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    @CallSuper
    private fun afterAll() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        testScope.cleanupTestCoroutines()
    }

    fun coroutineTest(block: TestCoroutineScope.() -> Unit) {
        runBlockingTest(testDispatcher) {
            block(this)
        }
    }
}
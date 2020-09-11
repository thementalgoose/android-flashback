package tmg.flashback.testutils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(CoroutineTestHandler::class)
open class BaseTest {

    @get:Rule
    open var coroutineRule: CoroutineRule = CoroutineRule()

    @get:Rule
    open val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    fun coroutineTest(body: suspend TestCoroutineScope.() -> Unit) {
        coroutineRule.runBlockingTest(body)
    }

    val testDispatcher: TestCoroutineDispatcher
        get() = coroutineRule.testDispatcher
}
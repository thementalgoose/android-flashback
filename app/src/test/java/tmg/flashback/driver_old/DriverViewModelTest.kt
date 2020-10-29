package tmg.flashback.driver_old

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.repo.db.stats.DriverDB
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertValue

@FlowPreview
@ExperimentalCoroutinesApi
class DriverViewModelTest: BaseTest() {

    lateinit var sut: DriverViewModel

    private val mockDriverDB: DriverDB = mock()

    @BeforeEach
    internal fun setUp() {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverview) })
    }

    private fun initSUT() {
        sut = DriverViewModel(mockDriverDB, testScopeProvider)
        sut.inputs.setup(mockDriverId)
    }

    @Test
    fun `DriverViewModel initialising driver with valid driver id shows season list emitted`() = coroutineTest {

        val expected: List<Int> = listOf(2019, 2018) // From MockData.kt

        initSUT()

        assertValue(expected, sut.outputs.seasons)
    }

    @Test
    fun `DriverViewModel initialising driver with invalid driver id shows empty list of season emitted`() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(null) })
        val expected: List<Int> = emptyList()

        initSUT()

        assertValue(expected, sut.outputs.seasons)
    }

    @AfterEach
    internal fun tearDown() {

        reset(mockDriverDB)
    }
}
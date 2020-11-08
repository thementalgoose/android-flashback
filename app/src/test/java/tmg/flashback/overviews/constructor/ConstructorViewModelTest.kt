package tmg.flashback.overviews.constructor

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.overviews.mockConstructorId
import tmg.flashback.repo.db.stats.ConstructorDB
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertDataEventValue

@FlowPreview
@ExperimentalCoroutinesApi
class ConstructorViewModelTest: BaseTest() {

    lateinit var sut: ConstructorViewModel

    private var mockConstructorDB: ConstructorDB = mock()
    private var mockConnectivityManager: ConnectivityManager = mock()

    @BeforeEach
    internal fun setUp() {

        whenever(mockConnectivityManager.isConnected).thenReturn(true)
    }

    private fun initSUT() {

        sut = ConstructorViewModel(mockConstructorDB, mockConnectivityManager, testScopeProvider)
        sut.inputs.setup(mockConstructorId)
    }

    @Test
    fun `ConstructorViewModel clicking open season opens season for driver`() = coroutineTest {

        val expected = 2020

        initSUT()

        sut.inputs.openSeason(expected)

        assertDataEventValue(Pair(mockConstructorId, expected), sut.outputs.openSeason)
    }

    @Test
    fun `ConstructorViewModel clicking open url forwards open url event`() = coroutineTest {

        val expectUrl = "http://www.google.com"

        initSUT()

        sut.inputs.openUrl(expectUrl)

        assertDataEventValue(expectUrl, sut.outputs.openUrl)
    }

    @AfterEach
    internal fun tearDown() {

        reset(mockConstructorDB, mockConnectivityManager)
    }
}
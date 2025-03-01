package tmg.flashback.search.presentation.circuits

import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.circuits.contract.Circuit
import tmg.flashback.circuits.contract.with
import tmg.flashback.data.repo.CircuitRepository
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.model
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.testutils.BaseTest

internal class SearchCircuitViewModelTest: BaseTest() {

    private val mockCircuitRepository: CircuitRepository = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)

    private lateinit var underTest: SearchCircuitViewModel

    private fun initUnderTest() {
        underTest = SearchCircuitViewModel(
            circuitRepository = mockCircuitRepository,
            navigator = mockNavigator
        )
    }

    private val dataRedBullRing = Circuit.model(id = "redbullring", name = "Red Bull Ring", city = "a")
    private val dataSilverstone = Circuit.model(id = "silverstone", name = "Silverstone", city = "b")
    private val dataSpa = Circuit.model(id = "spa", name = "Spa", city = "c")


    @BeforeEach
    fun setUp() {
        every { mockCircuitRepository.getCircuits() } returns flow {
            emit(listOf(dataSilverstone, dataSpa, dataRedBullRing))
        }
    }

    @Test
    fun `init will get list of content with ordered content`() = runTest {
        initUnderTest()

        underTest.uiState.test {
            val result = awaitItem()
            assertEquals("", result.searchTerm)
            assertEquals(listOf(dataRedBullRing, dataSilverstone, dataSpa), result.all)
            assertEquals(listOf(dataRedBullRing, dataSilverstone, dataSpa), result.filtered)
            assertEquals(false, result.isLoading)
        }
    }

    @Test
    fun `search term will filter content`() = runTest {
        initUnderTest()

        underTest.uiState.test {
            val result1 = awaitItem()
            assertEquals(listOf(dataRedBullRing, dataSilverstone, dataSpa), result1.filtered)
            assertEquals("", result1.searchTerm)

            underTest.searchTerm("s")

            val result2 = awaitItem()
            assertEquals(listOf(dataSilverstone, dataSpa), result2.filtered)

            underTest.searchTerm("sp")

            val result3 = awaitItem()
            assertEquals(listOf(dataSpa), result3.filtered)

            underTest.searchTerm("spa")

            val result4 = awaitItem()
            assertEquals(listOf(dataSpa), result4.filtered)

            underTest.searchTerm("")

            val result5 = awaitItem()
            assertEquals(listOf(dataRedBullRing, dataSilverstone, dataSpa), result5.filtered)
        }
    }

    @Test
    fun `click circuit will navigate to circuit`() {
        initUnderTest()

        val circuit = Circuit.model()

        underTest.clickCircuit(circuit)

        verify {
            mockNavigator.navigate(Screen.Circuit.with(circuit.id, circuit.name))
        }
    }

    @Test
    fun `refresh will call fetch circuits then refresh content`() = runTest {
        initUnderTest()

        underTest.uiState.test {
            val result1 = awaitItem()
            assertEquals(listOf(dataRedBullRing, dataSilverstone, dataSpa), result1.filtered)

            val updatedCircuitList = listOf(dataRedBullRing, dataSpa)
            every { mockCircuitRepository.getCircuits() } returns flow { emit(updatedCircuitList) }

            underTest.refresh()
            val result3 = awaitItem()
            assertEquals(false, result3.isLoading)
            assertEquals(updatedCircuitList, result3.all)
            assertEquals(updatedCircuitList, result3.filtered)
        }

        coVerify { mockCircuitRepository.fetchCircuits() }
    }

}
package tmg.flashback.statistics.ui.circuit

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.core.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.model.CircuitHistory
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.repo.CircuitRepository
import tmg.testutils.BaseTest

internal class CircuitInfoViewModelTest: BaseTest() {

    lateinit var sut: CircuitInfoViewModel

    private val mockCircuitRepository: CircuitRepository = mockk(relaxed = true)
    private val mockConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)

    @BeforeEach
    internal fun setUp() {
        every { mockConnectivityManager.isConnected } returns true
        every { mockCircuitRepository.getCircuitHistory(any()) } returns flow { emit(CircuitHistory.model()) }
        coEvery { mockCircuitRepository.getCircuitRounds(any()) } returns 1
    }

    private fun initSUT() {
        sut = CircuitInfoViewModel(mockCircuitRepository, mockConnectivityManager)
    }

    @Test
    fun `

    @Test
    fun `circuit request is not made when rounds found in DB`() = coroutineTest {
        coEvery { mockCircuitRepository.getCircuitRounds(any()) } returns 1

        initSUT()
        sut.inputs.circuitId("circuitId")

        coVerify(exactly = 0) {
            mockCircuitRepository.fetchCircuit(any())
        }
    }

    @Test
    fun `circuit request is made when no rounds found in DB`() = coroutineTest {
        coEvery { mockCircuitRepository.getCircuitRounds(any()) } returns 0

        initSUT()
        sut.inputs.circuitId("circuitId")

        coVerify {
            mockCircuitRepository.fetchCircuit(any())
        }
    }
}
package tmg.flashback.search.presentation.drivers

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
import tmg.flashback.data.repo.DriverRepository
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.model
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.testutils.BaseTest

internal class SearchDriverViewModelTest: BaseTest() {

    private val mockDriverRepository: DriverRepository = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)

    private lateinit var underTest: SearchDriverViewModel

    private fun initUnderTest() {
        underTest = SearchDriverViewModel(
            driverRepository = mockDriverRepository,
            navigator = mockNavigator
        )
    }

    private val dataPiastri = Driver.model(id = "piastri", firstName = "Oscar", lastName = "Piastri", nationality = "a")
    private val dataLeclerc = Driver.model(id = "leclerc", firstName = "Charles", lastName = "Leclerc", nationality = "n")
    private val dataNorris = Driver.model(id = "norris", firstName = "Lando", lastName = "Norris", nationality = "b")
    private val dataAlbon = Driver.model(id = "albon", firstName = "Alex", lastName = "Albon", nationality = "c")

    @BeforeEach
    fun setUp() {
        every { mockDriverRepository.getDrivers() } returns flow {
            emit(listOf(dataPiastri, dataLeclerc, dataNorris, dataAlbon))
        }
    }

    @Test
    fun `init will get list of content with ordered content`() = runTest {
        initUnderTest()

        underTest.uiState.test {
            val result = awaitItem()
            assertEquals("", result.searchTerm)
            assertEquals(listOf(dataAlbon, dataLeclerc, dataNorris, dataPiastri), result.all)
            assertEquals(listOf(dataAlbon, dataLeclerc, dataNorris, dataPiastri), result.filtered)
            assertEquals(false, result.isLoading)
        }
    }

    @Test
    fun `search term will filter content`() = runTest {
        initUnderTest()

        underTest.uiState.test {
            val result1 = awaitItem()
            assertEquals(listOf(dataAlbon, dataLeclerc, dataNorris, dataPiastri), result1.filtered)
            assertEquals("", result1.searchTerm)

            underTest.searchTerm("n")

            val result2 = awaitItem()
            assertEquals(listOf(dataAlbon, dataLeclerc, dataNorris), result2.filtered)

            underTest.searchTerm("no")

            val result3 = awaitItem()
            assertEquals(listOf(dataNorris), result3.filtered)

            underTest.searchTerm("norris")

            val result4 = awaitItem()
            assertEquals(listOf(dataNorris), result4.filtered)

            underTest.searchTerm("")

            val result5 = awaitItem()
            assertEquals(listOf(dataAlbon, dataLeclerc, dataNorris, dataPiastri), result5.filtered)
        }
    }

    @Test
    fun `click driver will navigate to driver`() {
        initUnderTest()

        val driver = Driver.model()

        underTest.clickDriver(driver)

        verify {
            mockNavigator.navigate(Screen.Driver(driver.id, driver.name))
        }
    }

    @Test
    fun `refresh will call fetch drivers then refresh content`() = runTest {
        initUnderTest()

        underTest.uiState.test {
            val result1 = awaitItem()
            assertEquals(listOf(dataAlbon, dataLeclerc, dataNorris, dataPiastri), result1.filtered)

            val updatedDriverList = listOf(dataAlbon, dataNorris)
            every { mockDriverRepository.getDrivers() } returns flow { emit(updatedDriverList) }

            underTest.refresh()
            val result3 = awaitItem()
            assertEquals(false, result3.isLoading)
            assertEquals(updatedDriverList, result3.all)
            assertEquals(updatedDriverList, result3.filtered)
        }

        coVerify { mockDriverRepository.fetchDrivers() }
    }
}

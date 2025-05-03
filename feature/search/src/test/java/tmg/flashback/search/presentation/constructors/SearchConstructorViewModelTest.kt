package tmg.flashback.search.presentation.constructors

import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.data.repo.ConstructorRepository
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.model
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.testutils.BaseTest

internal class SearchConstructorViewModelTest: BaseTest() {

    private val mockConstructorRepository: ConstructorRepository = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)

    private lateinit var underTest: SearchConstructorViewModel

    private fun initUnderTest() {
        underTest = SearchConstructorViewModel(
            constructorRepository = mockConstructorRepository,
            navigator = mockNavigator
        )
    }

    private val dataMclaren = Constructor.model(id = "mclaren", name = "Mclaren", nationality = "a")
    private val dataMercedes = Constructor.model(id = "mercedes", name = "Mercedes", nationality = "b")
    private val dataAlpine = Constructor.model(id = "alpine", name = "Alpine", nationality = "c")

    @BeforeEach
    fun setUp() {
        every { mockConstructorRepository.getConstructors() } returns flow {
            emit(listOf(dataAlpine, dataMercedes, dataMclaren))
        }
    }

    @Test
    fun `init will get list of content with ordered content`() = runTest {
        initUnderTest()

        underTest.uiState.test {
            val result = awaitItem()
            assertEquals("", result.searchTerm)
            assertEquals(listOf(dataAlpine, dataMclaren, dataMercedes), result.all)
            assertEquals(listOf(dataAlpine, dataMclaren, dataMercedes), result.filtered)
            assertEquals(false, result.isLoading)
        }
    }

    @Test
    fun `search term will filter content`() = runTest {
        initUnderTest()

        underTest.uiState.test {
            val result1 = awaitItem()
            assertEquals(listOf(dataAlpine, dataMclaren, dataMercedes), result1.filtered)
            assertEquals("", result1.searchTerm)

            underTest.searchTerm("m")

            val result2 = awaitItem()
            assertEquals(listOf(dataMclaren, dataMercedes), result2.filtered)

            underTest.searchTerm("me")

            val result3 = awaitItem()
            assertEquals(listOf(dataMercedes), result3.filtered)

            underTest.searchTerm("mercedes")

            val result4 = awaitItem()
            assertEquals(listOf(dataMercedes), result4.filtered)

            underTest.searchTerm("")

            val result5 = awaitItem()
            assertEquals(listOf(dataAlpine, dataMclaren, dataMercedes), result5.filtered)
        }
    }

    @Test
    fun `click constructor will navigate to constructor`() {
        initUnderTest()

        val constructor = Constructor.model()

        underTest.clickConstructor(constructor)

        verify {
            mockNavigator.navigate(Screen.Constructor(constructor.id, constructor.name))
        }
    }

    @Test
    fun `refresh will call fetch constructors then refresh content`() = runTest {
        initUnderTest()

        underTest.uiState.test {
            val result1 = awaitItem()
            assertEquals(listOf(dataAlpine, dataMclaren, dataMercedes), result1.filtered)

            val updatedConstructorList = listOf(dataAlpine, dataMclaren)
            every { mockConstructorRepository.getConstructors() } returns flow { emit(updatedConstructorList) }

            underTest.refresh()
            val result3 = awaitItem()
            assertEquals(false, result3.isLoading)
            assertEquals(updatedConstructorList, result3.all)
            assertEquals(updatedConstructorList, result3.filtered)
        }

        coVerify { mockConstructorRepository.fetchConstructors() }
    }
}
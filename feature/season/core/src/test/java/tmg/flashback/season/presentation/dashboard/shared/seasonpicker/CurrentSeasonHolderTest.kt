package tmg.flashback.season.presentation.dashboard.shared.seasonpicker

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.season.repository.HomeRepository
import tmg.flashback.season.usecases.DefaultSeasonUseCase
import tmg.testutils.BaseTest

internal class CurrentSeasonHolderTest: BaseTest() {

    private val mockDefaultSeasonUseCase: DefaultSeasonUseCase = mockk(relaxed = true)
    private val mockHomeRepository: HomeRepository = mockk(relaxed = true)

    private lateinit var underTest: CurrentSeasonHolder

    private fun initUnderTest() {
        underTest = CurrentSeasonHolder(
            defaultSeasonUseCase = mockDefaultSeasonUseCase,
            homeRepository = mockHomeRepository
        )
    }

    @BeforeEach
    fun setUp() {
        every { mockDefaultSeasonUseCase.defaultSeason } returns 2020
        every { mockHomeRepository.supportedSeasons } returns setOf(2020, 2021, 2022)
    }

    @Test
    fun `initial value of currentSeasonFlow and currentSeason is default value`() = runTest {
        initUnderTest()

        val expected = 2020
        assertEquals(expected, underTest.currentSeason)
        underTest.currentSeasonFlow.test {
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `initial value of supportedSeasonFlow and supportedSeason is default value`() = runTest {
        initUnderTest()

        val expected = listOf(2022, 2021, 2020)
        assertEquals(expected, underTest.supportedSeasons)
        underTest.supportedSeasonFlow.test {
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `updating season sets current value if in supported seasons`() {
        initUnderTest()
        assertEquals(listOf(2022, 2021, 2020), underTest.supportedSeasons)
        assertEquals(2020, underTest.currentSeason)

        every { mockHomeRepository.supportedSeasons } returns setOf(2021, 2022)
        underTest.updateTo(2022)

        assertEquals(2022, underTest.currentSeason)
        assertEquals(listOf(2022, 2021), underTest.supportedSeasons)

    }

    @Test
    fun `updating season does nothing if season is not supported`() {
        initUnderTest()
        assertEquals(listOf(2022, 2021, 2020), underTest.supportedSeasons)
        assertEquals(2020, underTest.currentSeason)

        every { mockHomeRepository.supportedSeasons } returns setOf(2021, 2022)
        underTest.updateTo(2019)

        assertEquals(2020, underTest.currentSeason) // Set to 2019 via. internal logic of DefaultSeasonUseCase!!
        assertEquals(listOf(2022, 2021), underTest.supportedSeasons)
    }
}
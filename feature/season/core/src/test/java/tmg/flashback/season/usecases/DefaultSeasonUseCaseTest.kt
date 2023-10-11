package tmg.flashback.season.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.season.repository.HomeRepository

internal class DefaultSeasonUseCaseTest {

    private val mockHomeRepository: HomeRepository = mockk(relaxed = true)

    private lateinit var underTest: DefaultSeasonUseCase

    private fun initUnderTest() {
        underTest = DefaultSeasonUseCase(mockHomeRepository)
    }

    //region Default year

    @Test
    fun `returns current year if supported season list is empty`() {
        every { mockHomeRepository.supportedSeasons } returns emptySet()
        every { mockHomeRepository.serverDefaultYear } returns 2018
        initUnderTest()

        assertEquals(Formula1.currentSeasonYear, underTest.defaultSeason)
    }

    @Test
    fun `returns user defined value if its supported`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2017, 2018)
        every { mockHomeRepository.serverDefaultYear } returns 2018
        initUnderTest()

        assertEquals(2018, underTest.defaultSeason)
    }

    @Test
    fun `runs clear default method if user defined value found to be invalid`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2019)
        every { mockHomeRepository.serverDefaultYear } returns 2018
        initUnderTest()

        assertEquals(2019, underTest.defaultSeason)
    }

    @Test
    fun `if user defined value invalid, return server value if in supported seasons`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2018,2019)
        every { mockHomeRepository.serverDefaultYear } returns 2018
        initUnderTest()

        assertEquals(2018, underTest.defaultSeason)
    }

    @Test
    fun `if user defined value is null, return server value if in supported seasons`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2018,2019)
        every { mockHomeRepository.serverDefaultYear } returns 2018
        initUnderTest()

        assertEquals(2018, underTest.defaultSeason)
    }

    @Test
    fun `if user defined value invalid or null, return max in supported seasons value if server value is not valid`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2018,2019)
        every { mockHomeRepository.serverDefaultYear } returns 2017
        initUnderTest()

        assertEquals(2019, underTest.defaultSeason)
    }

    @Test
    fun `default season server returns the server configured default season`() {
        every { mockHomeRepository.serverDefaultYear } returns 2018
        initUnderTest()
        assertEquals(2018, underTest.serverDefaultSeason)
        verify {
            mockHomeRepository.serverDefaultYear
        }
    }
    //endregion
}
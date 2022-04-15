package tmg.flashback.stats.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.stats.repository.HomeRepository

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
        every { mockHomeRepository.defaultSeason } returns 2017
        initUnderTest()

        assertEquals(Formula1.currentSeasonYear, underTest.defaultSeason)
    }

    @Test
    fun `returns user defined value if its supported`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2017, 2018)
        every { mockHomeRepository.serverDefaultYear } returns 2018
        every { mockHomeRepository.defaultSeason } returns 2017
        initUnderTest()

        assertEquals(2017, underTest.defaultSeason)
    }

    @Test
    fun `runs clear default method if user defined value found to be invalid`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2019)
        every { mockHomeRepository.serverDefaultYear } returns 2018
        every { mockHomeRepository.defaultSeason } returns 2017
        initUnderTest()

        assertEquals(2019, underTest.defaultSeason)
        verify {
            mockHomeRepository.defaultSeason = null
        }
    }

    @Test
    fun `if user defined value invalid, return server value if in supported seasons`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2018,2019)
        every { mockHomeRepository.serverDefaultYear } returns 2018
        every { mockHomeRepository.defaultSeason } returns 1921
        initUnderTest()

        assertEquals(2018, underTest.defaultSeason)
    }

    @Test
    fun `if user defined value is null, return server value if in supported seasons`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2018,2019)
        every { mockHomeRepository.serverDefaultYear } returns 2018
        every { mockHomeRepository.defaultSeason } returns null
        initUnderTest()

        assertEquals(2018, underTest.defaultSeason)
    }

    @Test
    fun `if user defined value invalid or null, return max in supported seasons value if server value is not valid`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2018,2019)
        every { mockHomeRepository.serverDefaultYear } returns 2017
        every { mockHomeRepository.defaultSeason } returns null
        initUnderTest()

        assertEquals(2019, underTest.defaultSeason)
    }

    @Test
    fun `set user default session updates user repository`() {
        initUnderTest()
        underTest.clearDefault()
        verify {
            mockHomeRepository.defaultSeason = null
        }
    }

    @Test
    fun `clear default sets default season to null in user repo`() {
        initUnderTest()
        underTest.setUserDefaultSeason(2018)
        verify {
            mockHomeRepository.defaultSeason = 2018
        }
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

    @Test
    fun `is user defined season value set value`() {
        every { mockHomeRepository.defaultSeason } returns 2018
        initUnderTest()
        assertTrue(underTest.isUserDefinedValueSet)
        verify {
            mockHomeRepository.defaultSeason
        }
    }

    @Test
    fun `is user defined season value set null`() {
        every { mockHomeRepository.defaultSeason } returns null
        initUnderTest()
        assertFalse(underTest.isUserDefinedValueSet)
        verify {
            mockHomeRepository.defaultSeason
        }
    }

    //endregion
}
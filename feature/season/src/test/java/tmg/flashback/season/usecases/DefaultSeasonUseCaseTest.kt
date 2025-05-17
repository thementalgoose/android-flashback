package tmg.flashback.season.usecases

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.season.repository.HomeRepository

internal class DefaultSeasonUseCaseTest {

    private val mockHomeRepository: HomeRepository = mockk(relaxed = true)

    private lateinit var underTest: DefaultSeasonUseCase

    private fun initUnderTest() {
        underTest = DefaultSeasonUseCase(mockHomeRepository)
    }

    @Test
    fun `returns current year if supported season list is empty`() {
        every { mockHomeRepository.supportedSeasons } returns emptySet()
        every { mockHomeRepository.defaultSeason } returns 2018
        initUnderTest()

        assertEquals(Formula1.currentSeasonYear, underTest.defaultSeason)
    }

    @Test
    fun `returns users last selected season if pref is enabled and season is supported`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2016, 2017, 2018)
        every { mockHomeRepository.userSelectedSeason } returns 2017
        every { mockHomeRepository.defaultSeason } returns 2016
        every { mockHomeRepository.keepUserSelectedSeason } returns true

        initUnderTest()

        assertEquals(2017, underTest.defaultSeason)
    }

    @Test
    fun `returns server season if season if users last selected season pref is disabled`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2016, 2017, 2018)
        every { mockHomeRepository.userSelectedSeason } returns 2017
        every { mockHomeRepository.defaultSeason } returns 2016
        every { mockHomeRepository.keepUserSelectedSeason } returns false

        initUnderTest()
        assertEquals(2016, underTest.defaultSeason)
    }

    @Test
    fun `returns server season if season is supported`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2016, 2017, 2018)
        every { mockHomeRepository.defaultSeason } returns 2017
        every { mockHomeRepository.keepUserSelectedSeason } returns false

        initUnderTest()
        assertEquals(2017, underTest.defaultSeason)
    }

    @Test
    fun `returns latest supported season if server season doesnt exist`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2016, 2017, 2018)
        every { mockHomeRepository.defaultSeason } returns 2020
        every { mockHomeRepository.keepUserSelectedSeason } returns false

        initUnderTest()
        assertEquals(2018, underTest.defaultSeason)
    }
}
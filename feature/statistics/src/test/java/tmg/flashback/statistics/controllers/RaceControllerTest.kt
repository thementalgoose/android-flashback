package tmg.flashback.statistics.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.statistics.repository.StatisticsRepository
import tmg.testutils.BaseTest

internal class RaceControllerTest: BaseTest() {

    private var mockStatisticsRepository: StatisticsRepository = mockk(relaxed = true)

    private lateinit var sut: RaceController

    private fun initSUT() {
        sut = RaceController(mockStatisticsRepository)
    }

    //region Show Qualifying Delta

    @Test
    fun `show qualifying delta reads from prefs`() {
        initSUT()
        every { mockStatisticsRepository.showQualifyingDelta } returns true
        assertTrue(mockStatisticsRepository.showQualifyingDelta)
        every { mockStatisticsRepository.showQualifyingDelta } returns false
        assertFalse(mockStatisticsRepository.showQualifyingDelta)

        verify(exactly = 2) {
            mockStatisticsRepository.showQualifyingDelta
        }
    }

    @Test
    fun `show qualifying update saves to prefs`() {
        initSUT()
        sut.showQualifyingDelta = true
        verify {
            mockStatisticsRepository.showQualifyingDelta = true
        }
    }

    //endregion

    //region Fade DNF

    @Test
    fun `fade dnf reads from prefs`() {
        initSUT()
        every { mockStatisticsRepository.fadeDNF } returns true
        assertTrue(mockStatisticsRepository.fadeDNF)
        every { mockStatisticsRepository.fadeDNF } returns false
        assertFalse(mockStatisticsRepository.fadeDNF)

        verify(exactly = 2) {
            mockStatisticsRepository.fadeDNF
        }
    }

    @Test
    fun `fade dnf saves to prefs`() {
        initSUT()
        sut.fadeDNF = true
        verify {
            mockStatisticsRepository.fadeDNF = true
        }
    }

    //endregion

    //region Show Grid Penalties

    @Test
    fun `show grid penalties in qualifying reads from prefs`() {
        initSUT()
        every { mockStatisticsRepository.showGridPenaltiesInQualifying } returns true
        assertTrue(mockStatisticsRepository.showGridPenaltiesInQualifying)
        every { mockStatisticsRepository.showGridPenaltiesInQualifying } returns false
        assertFalse(mockStatisticsRepository.showGridPenaltiesInQualifying)

        verify(exactly = 2) {
            mockStatisticsRepository.showGridPenaltiesInQualifying
        }
    }

    @Test
    fun `show grid penalties in qualifying saves to prefs`() {
        initSUT()
        sut.showGridPenaltiesInQualifying = true
        verify {
            mockStatisticsRepository.showGridPenaltiesInQualifying = true
        }
    }

    //endregion
}
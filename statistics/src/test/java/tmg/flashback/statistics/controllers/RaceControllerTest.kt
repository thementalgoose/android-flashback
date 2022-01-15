package tmg.flashback.statistics.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.flashback.statistics.repository.HomeRepository
import tmg.testutils.BaseTest

internal class RaceControllerTest: BaseTest() {

    private var mockHomeRepository: HomeRepository = mockk(relaxed = true)

    private lateinit var sut: RaceController

    private fun initSUT() {
        sut = RaceController(mockHomeRepository)
    }

    //region Show Qualifying Delta

    @Test
    fun `show qualifying delta reads from prefs`() {
        initSUT()
        every { mockHomeRepository.showQualifyingDelta } returns true
        assertTrue(mockHomeRepository.showQualifyingDelta)
        every { mockHomeRepository.showQualifyingDelta } returns false
        assertFalse(mockHomeRepository.showQualifyingDelta)

        verify(exactly = 2) {
            mockHomeRepository.showQualifyingDelta
        }
    }

    @Test
    fun `show qualifying update saves to prefs`() {
        initSUT()
        sut.showQualifyingDelta = true
        verify {
            mockHomeRepository.showQualifyingDelta = true
        }
    }

    //endregion

    //region Fade DNF

    @Test
    fun `fade dnf reads from prefs`() {
        initSUT()
        every { mockHomeRepository.fadeDNF } returns true
        assertTrue(mockHomeRepository.fadeDNF)
        every { mockHomeRepository.fadeDNF } returns false
        assertFalse(mockHomeRepository.fadeDNF)

        verify(exactly = 2) {
            mockHomeRepository.fadeDNF
        }
    }

    @Test
    fun `fade dnf saves to prefs`() {
        initSUT()
        sut.fadeDNF = true
        verify {
            mockHomeRepository.fadeDNF = true
        }
    }

    //endregion

    //region Show Grid Penalties

    @Test
    fun `show grid penalties in qualifying reads from prefs`() {
        initSUT()
        every { mockHomeRepository.showGridPenaltiesInQualifying } returns true
        assertTrue(mockHomeRepository.showGridPenaltiesInQualifying)
        every { mockHomeRepository.showGridPenaltiesInQualifying } returns false
        assertFalse(mockHomeRepository.showGridPenaltiesInQualifying)

        verify(exactly = 2) {
            mockHomeRepository.showGridPenaltiesInQualifying
        }
    }

    @Test
    fun `show grid penalties in qualifying saves to prefs`() {
        initSUT()
        sut.showGridPenaltiesInQualifying = true
        verify {
            mockHomeRepository.showGridPenaltiesInQualifying = true
        }
    }

    //endregion
}
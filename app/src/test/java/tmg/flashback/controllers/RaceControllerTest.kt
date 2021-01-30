package tmg.flashback.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.data.repositories.AppRepository
import tmg.flashback.testutils.BaseTest

internal class RaceControllerTest: BaseTest() {

    private var mockAppRepository: AppRepository = mockk(relaxed = true)

    private lateinit var sut: RaceController

    private fun initSUT() {
        sut = RaceController(mockAppRepository)
    }

    //region Show Qualifying Delta

    @Test
    fun `RaceController show qualifying delta reads from prefs`() {
        initSUT()
        every { mockAppRepository.showQualifyingDelta } returns true
        assertTrue(mockAppRepository.showQualifyingDelta)
        every { mockAppRepository.showQualifyingDelta } returns false
        assertFalse(mockAppRepository.showQualifyingDelta)

        verify(exactly = 2) {
            mockAppRepository.showQualifyingDelta
        }
    }

    @Test
    fun `RaceController show qualifying update saves to prefs`() {
        initSUT()
        sut.showQualifyingDelta = true
        verify {
            mockAppRepository.showQualifyingDelta = true
        }
    }

    //endregion

    //region Fade DNF

    @Test
    fun `RaceController fade dnf reads from prefs`() {
        initSUT()
        every { mockAppRepository.fadeDNF } returns true
        assertTrue(mockAppRepository.fadeDNF)
        every { mockAppRepository.fadeDNF } returns false
        assertFalse(mockAppRepository.fadeDNF)

        verify(exactly = 2) {
            mockAppRepository.fadeDNF
        }
    }

    @Test
    fun `RaceController fade dnf saves to prefs`() {
        initSUT()
        sut.fadeDNF = true
        verify {
            mockAppRepository.fadeDNF = true
        }
    }

    //endregion

    //region Show Grid Penalties

    @Test
    fun `RaceController show grid penalties in qualifying reads from prefs`() {
        initSUT()
        every { mockAppRepository.showGridPenaltiesInQualifying } returns true
        assertTrue(mockAppRepository.showGridPenaltiesInQualifying)
        every { mockAppRepository.showGridPenaltiesInQualifying } returns false
        assertFalse(mockAppRepository.showGridPenaltiesInQualifying)

        verify(exactly = 2) {
            mockAppRepository.showGridPenaltiesInQualifying
        }
    }

    @Test
    fun `RaceController show grid penalties in qualifying saves to prefs`() {
        initSUT()
        sut.showGridPenaltiesInQualifying = true
        verify {
            mockAppRepository.showGridPenaltiesInQualifying = true
        }
    }

    //endregion
}
package tmg.flashback.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.data.pref.UserRepository
import tmg.flashback.testutils.BaseTest

internal class RaceControllerTest: BaseTest() {

    private var mockUserRepository: UserRepository = mockk(relaxed = true)

    private lateinit var sut: RaceController

    private fun initSUT() {
        sut = RaceController(mockUserRepository)
    }

    //region Show Qualifying Delta

    @Test
    fun `RaceController show qualifying delta reads from prefs`() {
        initSUT()
        every { mockUserRepository.showQualifyingDelta } returns true
        assertTrue(mockUserRepository.showQualifyingDelta)
        every { mockUserRepository.showQualifyingDelta } returns false
        assertFalse(mockUserRepository.showQualifyingDelta)

        verify(exactly = 2) {
            mockUserRepository.showQualifyingDelta
        }
    }

    @Test
    fun `RaceController show qualifying update saves to prefs`() {
        initSUT()
        sut.showQualifyingDelta = true
        verify {
            mockUserRepository.showQualifyingDelta = true
        }
    }

    //endregion

    //region Fade DNF

    @Test
    fun `RaceController fade dnf reads from prefs`() {
        initSUT()
        every { mockUserRepository.fadeDNF } returns true
        assertTrue(mockUserRepository.fadeDNF)
        every { mockUserRepository.fadeDNF } returns false
        assertFalse(mockUserRepository.fadeDNF)

        verify(exactly = 2) {
            mockUserRepository.fadeDNF
        }
    }

    @Test
    fun `RaceController fade dnf saves to prefs`() {
        initSUT()
        sut.fadeDNF = true
        verify {
            mockUserRepository.fadeDNF = true
        }
    }

    //endregion

    //region Show Grid Penalties

    @Test
    fun `RaceController show grid penalties in qualifying reads from prefs`() {
        initSUT()
        every { mockUserRepository.showGridPenaltiesInQualifying } returns true
        assertTrue(mockUserRepository.showGridPenaltiesInQualifying)
        every { mockUserRepository.showGridPenaltiesInQualifying } returns false
        assertFalse(mockUserRepository.showGridPenaltiesInQualifying)

        verify(exactly = 2) {
            mockUserRepository.showGridPenaltiesInQualifying
        }
    }

    @Test
    fun `RaceController show grid penalties in qualifying saves to prefs`() {
        initSUT()
        sut.showGridPenaltiesInQualifying = true
        verify {
            mockUserRepository.showGridPenaltiesInQualifying = true
        }
    }

    //endregion
}
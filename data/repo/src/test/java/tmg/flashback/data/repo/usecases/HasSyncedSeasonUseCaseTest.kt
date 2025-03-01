package tmg.flashback.data.repo.usecases

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.data.repo.RaceRepository

class HasSyncedSeasonUseCaseTest {

    private val mockRacesRepository: RaceRepository = mockk(relaxed = true)

    private lateinit var underTest: HasSyncedSeasonUseCase

    private fun initUnderTest() {
        underTest = HasSyncedSeasonUseCase(
            racesRepository = mockRacesRepository
        )
    }

    @Test
    fun `has synced returns status from races repository`() {
        every { mockRacesRepository.hasntPreviouslySynced(2020) } returns true
        every { mockRacesRepository.hasntPreviouslySynced(2021) } returns false

        initUnderTest()

        assertEquals(false, underTest.hasSynced(2020))
        assertEquals(true, underTest.hasSynced(2021))
    }
}
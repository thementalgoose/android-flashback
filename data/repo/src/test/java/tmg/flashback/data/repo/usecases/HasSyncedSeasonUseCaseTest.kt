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
        every { mockRacesRepository.hasPreviouslySynced(2020) } returns false
        every { mockRacesRepository.hasPreviouslySynced(2021) } returns true

        initUnderTest()

        assertEquals(false, underTest.hasSynced(2020))
        assertEquals(true, underTest.hasSynced(2021))
    }
}
package tmg.flashback.statistics.repo.mappers.app

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.RoomRace
import tmg.flashback.formula1.model.Race
import tmg.flashback.statistics.room.models.overview.model

internal class SeasonMapperTest {

    private val mockRaceMapper: RaceMapper = mockk(relaxed = true)

    private lateinit var sut: SeasonMapper

    @BeforeEach
    internal fun setUp() {
        sut = SeasonMapper(mockRaceMapper)

        every { mockRaceMapper.mapRace(any<RoomRace>()) } returns Race.model()
    }

    @Test
    fun `mapSeason maps fields correctly`() {
        val inputSeason: Int = 2020
        val inputRaces = listOf(RoomRace.model())
        val expected = tmg.flashback.statistics.room.models.overview.model()

        assertEquals(expected, sut.mapSeason(inputSeason, inputRaces))
    }
}
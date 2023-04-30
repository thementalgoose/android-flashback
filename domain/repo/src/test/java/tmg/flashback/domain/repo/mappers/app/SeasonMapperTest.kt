package tmg.flashback.domain.repo.mappers.app

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.RoomRace
import tmg.flashback.formula1.model.Event
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.Season
import tmg.flashback.formula1.model.model
import tmg.flashback.domain.persistence.models.overview.model
import tmg.flashback.domain.persistence.models.race.model

internal class SeasonMapperTest {

    private val mockRaceMapper: RaceMapper = mockk(relaxed = true)
    private val mockEventMapper: EventMapper = mockk(relaxed = true)

    private lateinit var sut: SeasonMapper

    @BeforeEach
    internal fun setUp() {
        sut = SeasonMapper(mockRaceMapper, mockEventMapper)

        every { mockEventMapper.mapEvent(any()) } returns Event.model()
        every { mockRaceMapper.mapRace(any<RoomRace>()) } returns Race.model()
    }

    @Test
    fun `mapSeason maps fields correctly`() {
        val inputSeason: Int = 2020
        val inputRaces = listOf(RoomRace.model())
        val inputEvents = listOf(tmg.flashback.domain.persistence.models.overview.Event.model())
        val expected = Season.model()

        assertEquals(expected, sut.mapSeason(inputSeason, inputRaces, inputEvents))
    }
}
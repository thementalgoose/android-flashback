package tmg.flashback.statistics.repo.mappers.network

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.NetworkRaceData
import tmg.flashback.statistics.room.models.race.RaceInfo
import tmg.flashback.statistics.room.models.overview.model

internal class NetworkRaceDataMapperTest {

    private lateinit var sut: NetworkRaceDataMapper

    @BeforeEach
    internal fun setUp() {
        sut = NetworkRaceDataMapper()
    }

    @Test
    fun `mapRaceData maps fields correctly`() {
        val input = NetworkRaceData.model()
        val expected = RaceInfo.model()

        assertEquals(expected, sut.mapRaceData(input))
    }
}
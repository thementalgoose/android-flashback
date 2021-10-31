package tmg.flashback.statistics.repo.mappers.network

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.statistics.network.models.overview.OverviewRace
import tmg.flashback.statistics.network.models.overview.model
import tmg.flashback.statistics.room.models.overview.Overview
import tmg.flashback.statistics.room.models.overview.model

internal class NetworkOverviewMapperTest {

    private lateinit var sut: NetworkOverviewMapper

    @BeforeEach
    internal fun setUp() {
        sut = NetworkOverviewMapper()
    }

    @Test
    fun `NetworkOverviewMapper mapOverview maps fields correctly`() {
        val input = OverviewRace.model()
        val expected = Overview.model()

        assertEquals(expected, sut.mapOverview(input))
    }

    @Test
    fun `NetworkOverviewMapper mapOverview null when input is null`() {
        assertNull(sut.mapOverview(null))
    }
}
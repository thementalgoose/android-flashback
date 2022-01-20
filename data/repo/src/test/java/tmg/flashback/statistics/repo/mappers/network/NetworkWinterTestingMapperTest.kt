package tmg.flashback.statistics.repo.mappers.network

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.statistics.network.models.races.WinterTesting
import tmg.flashback.statistics.network.models.races.model
import tmg.flashback.statistics.room.models.race.model

internal class NetworkWinterTestingMapperTest {

    private lateinit var sut: NetworkWinterTestingMapper

    @BeforeEach
    internal fun setUp() {
        sut = NetworkWinterTestingMapper()
    }

    @Test
    fun `mapWinterTesting maps fields correctly`() {
        val input = WinterTesting.model()
        val expected = tmg.flashback.statistics.room.models.race.WinterTesting.model()

        assertEquals(expected, sut.mapWinterTesting(2020, input))
    }

    @Test
    fun `mapWinterTesting returns null if date is invalid`() {
        val input = WinterTesting.model(
            date = "invalid"
        )

        assertNull(sut.mapWinterTesting(2020, input))
    }
}
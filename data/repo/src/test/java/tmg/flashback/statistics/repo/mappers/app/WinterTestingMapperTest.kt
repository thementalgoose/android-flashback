package tmg.flashback.statistics.repo.mappers.app

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.room.models.race.WinterTesting
import tmg.flashback.statistics.room.models.race.model

internal class WinterTestingMapperTest {

    private lateinit var sut: WinterTestingMapper

    @BeforeEach
    internal fun setUp() {
        sut = WinterTestingMapper()
    }

    @Test
    fun `mapWinterTesting maps fields correctly`() {
        val inputModel = WinterTesting.model()
        val expected = tmg.flashback.formula1.model.WinterTesting.model()

        assertEquals(expected, sut.mapWinterTesting(inputModel))
    }

    @Test
    fun `mapWinterTesting returns null when date is invalid`() {
        val inputModel = WinterTesting.model(
            date = "invalid"
        )

        assertNull(sut.mapWinterTesting(inputModel))
    }
}
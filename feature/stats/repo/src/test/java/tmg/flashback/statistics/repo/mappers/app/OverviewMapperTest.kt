package tmg.flashback.statistics.repo.mappers.app

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.format.DateTimeParseException
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.room.models.overview.Overview
import tmg.flashback.statistics.room.models.overview.model

internal class OverviewMapperTest {

    private lateinit var sut: OverviewMapper

    @BeforeEach
    internal fun setUp() {
        sut = OverviewMapper()
    }

    @Test
    fun `OverviewMapper mapOverview maps fields correctly`() {
        val input = Overview.model()
        val expected = OverviewRace.model()

        assertEquals(expected, sut.mapOverview(input))
    }

    @Test
    fun `OverviewMapper mapOverview throws error if date is invalid`() {
        val input = Overview.model(date = "invalid")

        assertThrows(DateTimeParseException::class.java) {
            sut.mapOverview(input)
        }
    }

    @Test
    fun `OverviewMapper mapOverview returns null time if invalid`() {
        val input = Overview.model(time = "invalid")

        assertNull(sut.mapOverview(input).time)
    }
}
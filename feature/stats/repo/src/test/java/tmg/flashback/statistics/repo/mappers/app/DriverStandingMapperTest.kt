package tmg.flashback.statistics.repo.mappers.app

import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.formula1.model.SeasonDriverStandings
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.room.models.standings.DriverStandingWithConstructors
import tmg.flashback.statistics.room.models.standings.model

internal class DriverStandingMapperTest {

    private val mockDriverDataMapper: DriverDataMapper = mockk(relaxed = true)
    private val mockConstructorDataMapper: ConstructorDataMapper = mockk(relaxed = true)

    private lateinit var sut: DriverStandingMapper

    @BeforeEach
    internal fun setUp() {
        sut = DriverStandingMapper(
            mockDriverDataMapper,
            mockConstructorDataMapper
        )
    }

    @Test
    fun `mapDriverStanding model maps fields correctly`() {
        val input = DriverStandingWithConstructors.model()
        val expected = SeasonDriverStandingSeason.model()

        assertEquals(expected, sut.mapDriverStanding(input))
    }

    @Test
    fun `mapDriverStanding list maps fields correctly`() {
        val input = listOf(DriverStandingWithConstructors.model())
        val expected = SeasonDriverStandings.model()

        assertEquals(expected, sut.mapDriverStanding(input))
    }

    @Test
    fun `mapDriverStanding list list returns null if list is empty`() {
        val input = emptyList<DriverStandingWithConstructors>()

        assertNull(sut.mapDriverStanding(input))
    }
}
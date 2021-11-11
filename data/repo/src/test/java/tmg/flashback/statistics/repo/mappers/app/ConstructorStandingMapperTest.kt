package tmg.flashback.statistics.repo.mappers.app

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.formula1.model.*
import tmg.flashback.statistics.room.models.standings.ConstructorStandingWithDrivers
import tmg.flashback.statistics.room.models.standings.model

internal class ConstructorStandingMapperTest {

    private val mockConstructorDataMapper: ConstructorDataMapper = mockk(relaxed = true)
    private val mockDriverDataMapper: DriverDataMapper = mockk(relaxed = true)

    private lateinit var sut: ConstructorStandingMapper

    @BeforeEach
    internal fun setUp() {
        sut = ConstructorStandingMapper(
            mockDriverDataMapper,
            mockConstructorDataMapper
        )

        every { mockDriverDataMapper.mapDriver(any()) } returns Driver.model()
        every { mockConstructorDataMapper.mapConstructorData(any()) } returns Constructor.model()
    }

    @Test
    fun `mapConstructorStanding model maps fields correctly`() {
        val input = ConstructorStandingWithDrivers.model()
        val expected = SeasonConstructorStandingSeason.model()

        assertEquals(expected, sut.mapConstructorStanding(input))
    }

    @Test
    fun `mapConstructorStanding list maps fields correctly`() {
        val input = listOf(ConstructorStandingWithDrivers.model())
        val expected = SeasonConstructorStandings.model()

        assertEquals(expected, sut.mapConstructorStanding(input))
    }

    @Test
    fun `mapConstructorStanding list returns null if list is empty`() {
        val input = emptyList<ConstructorStandingWithDrivers>()

        assertNull(sut.mapConstructorStanding(input))
    }

}
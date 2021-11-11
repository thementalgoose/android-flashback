package tmg.flashback.statistics.repo.mappers.app

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.RoomDriverHistory
import tmg.flashback.formula1.model.*
import tmg.flashback.statistics.room.models.drivers.model

internal class DriverMapperTest {

    private val mockDriverDataMapper: DriverDataMapper = mockk(relaxed = true)
    private val mockConstructorDataMapper: ConstructorDataMapper = mockk(relaxed = true)
    private val mockRaceMapper: RaceMapper = mockk(relaxed = true)

    private lateinit var sut: DriverMapper

    @BeforeEach
    internal fun setUp() {
        sut = DriverMapper(
            mockDriverDataMapper,
            mockConstructorDataMapper,
            mockRaceMapper
        )

        every { mockDriverDataMapper.mapDriver(any()) } returns Driver.model()
        every { mockConstructorDataMapper.mapConstructorData(any()) } returns Constructor.model()
        every { mockRaceMapper.mapRaceInfoWithCircuit(any()) } returns RaceInfo.model()
    }

    @Test
    fun `mapDriver maps fields correctly`() {
        val input = RoomDriverHistory.model()
        val expected = DriverHistory.model()

        assertEquals(expected, sut.mapDriver(input))
    }

    @Test
    fun `mapDriver returns null if input is null`() {
        assertNull(sut.mapDriver(null))
    }
}
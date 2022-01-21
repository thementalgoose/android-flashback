package tmg.flashback.statistics.repo.mappers.app

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.format.DateTimeParseException
import tmg.flashback.RoomRace
import tmg.flashback.RoomRaceInfo
import tmg.flashback.RoomRaceInfoWithCircuit
import tmg.flashback.formula1.model.*
import tmg.flashback.statistics.room.models.overview.model

internal class RaceMapperTest {

    private val mockCircuitMapper: CircuitMapper = mockk(relaxed = true)
    private val mockDriverDataMapper: DriverDataMapper = mockk(relaxed = true)
    private val mockConstructorDataMapper: ConstructorDataMapper = mockk(relaxed = true)
    private val mockScheduleMapper: ScheduleMapper = mockk(relaxed = true)

    private lateinit var sut: RaceMapper

    @BeforeEach
    internal fun setUp() {
        sut = RaceMapper(
            mockCircuitMapper,
            mockDriverDataMapper,
            mockConstructorDataMapper,
            mockScheduleMapper
        )

        every { mockCircuitMapper.mapCircuit(any()) } returns Circuit.model()
        every { mockDriverDataMapper.mapDriver(any()) } returns Driver.model()
        every { mockConstructorDataMapper.mapConstructorData(any()) } returns Constructor.model()
        every { mockScheduleMapper.mapSchedule(any()) } returns Schedule.model()
    }

    @Test
    fun `mapRaceInfo maps fields correctly`() {
        val input = RoomRace.model()
        val expected = RaceInfo.model()

        assertEquals(expected, sut.mapRaceInfo(input))
    }

    @Test
    fun `mapRaceInfo throws error if date is invalid`() {
        val input = RoomRace.model(raceInfo = RoomRaceInfo.model(date = "invalid"))

        assertThrows(DateTimeParseException::class.java) {
            sut.mapRaceInfo(input)
        }
    }

    @Test
    fun `mapRaceInfo returns null if time is invalid`() {
        val input = RoomRace.model(raceInfo = RoomRaceInfo.model(time = "invalid"))

        assertNull(sut.mapRaceInfo(input).time)
    }

    @Test
    fun `mapRaceInfoWithCircuit maps fields correctly`() {
        val input = RoomRaceInfoWithCircuit.model()
        val expected = RaceInfo.model()

        assertEquals(expected, sut.mapRaceInfoWithCircuit(input))
    }

    @Test
    fun `mapRaceInfoWithCircuit throws error if date is invalid`() {
        val input = RoomRaceInfoWithCircuit.model(raceInfo = RoomRaceInfo.model(date = "invalid"))

        assertThrows(DateTimeParseException::class.java) {
            sut.mapRaceInfoWithCircuit(input)
        }
    }

    @Test
    fun `mapRaceInfoWithCircuit returns null if time is invalid`() {
        val input = RoomRaceInfoWithCircuit.model(raceInfo = RoomRaceInfo.model(time = "invalid"))

        assertNull(sut.mapRaceInfoWithCircuit(input).time)
    }

    @Test
    fun `mapRace maps fields correctly`() {
        val input = RoomRace.model()
        val expected = Race.model()

        assertEquals(expected, sut.mapRace(input))
    }
}
package tmg.flashback.firebase.mappers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.CircuitHistoryRace
import tmg.flashback.formula1.model.Location
import tmg.flashback.firebase.models.FCircuit
import tmg.flashback.firebase.models.FCircuitResult
import tmg.flashback.firebase.models.model
import tmg.testutils.BaseTest

internal class CircuitMapperTest: BaseTest() {

    private val mockCrashController: CrashController = mockk(relaxed = true)
    private val mockLocationMapper: LocationMapper = mockk(relaxed = true)

    private lateinit var sut: CircuitMapper

    private fun initSUT() {
        sut = CircuitMapper(mockCrashController, mockLocationMapper)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockLocationMapper.mapCircuitLocation(any()) } returns tmg.flashback.formula1.model.Location(
            1.0,
            2.0
        )
    }

    @Test
    fun `Circuit maps fields correctly`() {
        initSUT()

        val input = FCircuit.model()
        val expected = Circuit(
            id = "circuitId",
            name = "circuitName",
            country = "country",
            countryISO = "countryISO",
            locality = "locality",
            location = Location(1.0, 2.0),
            wikiUrl = "wikiUrl",
            results = listOf(
                CircuitHistoryRace(
                    name = "name",
                    season = 2020,
                    round = 1,
                    wikiUrl = "wikiUrl",
                    date = LocalDate.of(2020, 1, 1),
                    time = LocalTime.of(12, 0, 0)
                )
            )
        )

        assertEquals(expected, sut.mapCircuit(input))
    }

    @Test
    fun `Circuit get location defaults to fallback values`() {
        initSUT()
        every { mockLocationMapper.mapCircuitLocation(any()) } returns null

        val input = FCircuit.model(
            location = null,
            locationLat = 4.0,
            locationLng = 5.0
        )
        assertEquals(4.0, sut.mapCircuit(input).location!!.lat)
        assertEquals(5.0, sut.mapCircuit(input).location!!.lng)
    }

    @Test
    fun `Circuit country iso defaults to empty is null`() {
        initSUT()

        val input = FCircuit.model(countryISO = null)
        assertEquals("", sut.mapCircuit(input).countryISO)
    }

    @Test
    fun `Circuit results get ignored if found to be null`() {
        initSUT()

        val input = FCircuit.model(results = mapOf(
            "s2020r1" to FCircuitResult.model(),
            "s2020r2" to FCircuitResult.model(date = "invalid")
        ))
        assertEquals(1, sut.mapCircuit(input).results.size)
    }

    @Test
    fun `CircuitRace maps fields correctly`() {
        initSUT()

        val input = FCircuitResult.model()
        val expected = CircuitHistoryRace(
            name = "name",
            season = 2020,
            round = 1,
            wikiUrl = "wikiUrl",
            date = LocalDate.of(2020, 1, 1),
            time = LocalTime.of(12, 0, 0)
        )

        assertEquals(expected, sut.mapCircuitRace("circuitId", input))
    }

    @Test
    fun `CircuitRace date logs exception if invalid`() {
        initSUT()

        val input = FCircuitResult.model(date = "invalid")
        assertNull(sut.mapCircuitRace("circuitId", input))
        verify {
            mockCrashController.logException(any())
        }
    }

    @Test
    fun `CircuitRace date is converted properly`() {
        initSUT()

        val input = FCircuitResult.model(date = "2020-02-02")
        val expected = LocalDate.of(2020, 2, 2)
        assertEquals(expected, sut.mapCircuitRace("circuitId", input)!!.date)
    }

    @Test
    fun `CircuitRace time is set to null if invalid time`() {
        initSUT()

        val input = FCircuitResult.model(time = "invalid")
        assertNull(sut.mapCircuitRace("circuitId", input)!!.time)
    }

    @Test
    fun `CircuitRace time is converted properly`() {
        initSUT()

        val input = FCircuitResult.model(time = "12:00:00")
        val expected = LocalTime.of(12, 0, 0)
        assertEquals(expected, sut.mapCircuitRace("circuitId", input)!!.time)
    }
}
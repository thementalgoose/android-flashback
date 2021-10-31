package tmg.flashback.firebase.mappers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.formula1.model.Location
import tmg.flashback.firebase.models.FCircuitLocation
import tmg.flashback.firebase.models.model
import tmg.testutils.BaseTest

internal class LocationMapperTest: BaseTest() {

    private lateinit var sut: LocationMapper

    private fun initSUT() {
        sut = LocationMapper()
    }

    @Test
    fun `Location maps fields correctly`() {
        initSUT()

        val input = FCircuitLocation.model()
        val expected = tmg.flashback.formula1.model.Location(
            lat = 1.0,
            lng = 2.0
        )

        assertEquals(expected, sut.mapCircuitLocation(input))
    }

    @Test
    fun `Location location is null if location is null`() {
        initSUT()

        val input = null
        assertNull(sut.mapCircuitLocation(input))
    }

    @Test
    fun `Location location is null if lat is invalid string`() {
        initSUT()

        val input = FCircuitLocation.model(lat = "invalid")
        assertNull(sut.mapCircuitLocation(input))
    }

    @Test
    fun `Location location is null if lng is invalid string`() {
        initSUT()

        val input = FCircuitLocation.model(lng = "invalid")
        assertNull(sut.mapCircuitLocation(input))
    }

    @Test
    fun `Location location is null if lat is null`() {
        initSUT()

        val input = FCircuitLocation.model(lat = null)
        assertNull(sut.mapCircuitLocation(input))
    }

    @Test
    fun `Location location is null if lng is null`() {
        initSUT()

        val input = FCircuitLocation.model(lng = null)
        assertNull(sut.mapCircuitLocation(input))
    }
}
package tmg.flashback.firebase.converters

import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.firebase.models.FConstructorOverviewStandingsDriver
import tmg.flashback.firebase.testutils.BaseTest
import tmg.flashback.repo.models.ConstructorDriver

internal class ConstructorOverviewConverterTest: BaseTest() {

    @Test
    fun `ConstructorOverviewDriver null driver number defaults to 0`() {

    }

    @Test
    fun `ConstructorOverviewDriverStanding null values always default to 0`() {

        val constructorDriver: ConstructorDriver = mockk(relaxed = true)
        val model = FConstructorOverviewStandingsDriver(null, null, null, null, null, null, null, null, null, null, null, null, null).convert(constructorDriver)
        assertEquals(-1, model.bestFinish)
        assertEquals(-1, model.bestQualifying)
        assertEquals(0, model.points)
        assertEquals(0, model.finishesInP1)
        assertEquals(0, model.finishesInP2)
        assertEquals(0, model.finishesInP3)
        assertEquals(0, model.finishesInPoints)
        assertEquals(0, model.qualifyingP1)
        assertEquals(0, model.qualifyingP2)
        assertEquals(0, model.qualifyingP3)
        assertNull(model.qualifyingTop10)
        assertEquals(0, model.races)
        assertEquals(-1, model.championshipStanding)
    }
}

package tmg.flashback.firebase.converters

import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.firebase.currentYear
import tmg.flashback.firebase.models.FConstructorOverviewData
import tmg.flashback.firebase.models.FConstructorOverviewStandings
import tmg.flashback.firebase.models.FConstructorOverviewStandingsDriver
import tmg.flashback.firebase.testutils.BaseTest
import tmg.flashback.data.models.ConstructorDriver

internal class ConstructorOverviewConverterTest: BaseTest() {

    @Test
    fun `in progress and current year shows true`() {

        val mockOverviewData = mockk<FConstructorOverviewData>()
        val model = FConstructorOverviewStandings(inProgress = true, s = currentYear)
        assertTrue(model.convert(mockOverviewData, emptyMap()).isInProgress)
    }

    @Test
    fun `in progress is true in old year means progress is false`() {

        val mockOverviewData = mockk<FConstructorOverviewData>()
        val model = FConstructorOverviewStandings(inProgress = true, s = 2019)
        assertFalse(model.convert(mockOverviewData, emptyMap()).isInProgress)
    }

    @Test
    fun `null values always default to 0`() {

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

package tmg.flashback.firebase.converters

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.firebase.models.FAllSeason
import tmg.flashback.firebase.models.FAllSeasons
import tmg.flashback.firebase.testutils.BaseTest

internal class AllSeasonConverterTest: BaseTest() {

    @Test
    fun `AllSeasonConverter map to empty set if seasons is empty`() {
        assertEquals(FAllSeasons(null).convert(), emptySet<Int>())
    }

    @Test
    fun `AllSeasonConverter map to empty set if seasons only contains nulls`() {
        assertEquals(FAllSeasons(listOf(FAllSeason(null))).convert(), emptySet<Int>())
    }
}
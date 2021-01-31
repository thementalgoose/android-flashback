package tmg.flashback.firebase.converters

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.firebase.currentYear
import tmg.flashback.firebase.testutils.BaseTest

internal class HistoryConverterTest: BaseTest() {

    @Test
    fun `all data up too is one less than this year when all data confirmed`() {

        assertEquals(currentYear - 1, allDataUpToo)
    }

}
package tmg.flashback.firebase.mappers.seasonoverview

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.firebase.models.FSeasonOverviewConstructor
import tmg.flashback.firebase.models.model
import tmg.testutils.BaseTest

internal class SeasonOverviewConstructorMapperTest: BaseTest() {

    private lateinit var sut: SeasonOverviewConstructorMapper

    private fun initSUT() {
        sut = SeasonOverviewConstructorMapper()
    }

    @Test
    fun `Constructor maps fields correctly`() {
        initSUT()

        val input = FSeasonOverviewConstructor.model()
        val expected = tmg.flashback.formula1.model.Constructor(
            id = "constructorId",
            name = "constructorName",
            wikiUrl = "wikiUrl",
            nationality = "nationality",
            nationalityISO = "nationalityISO",
            color = 0
        )

        assertEquals(expected, sut.mapConstructor(input))
    }
}
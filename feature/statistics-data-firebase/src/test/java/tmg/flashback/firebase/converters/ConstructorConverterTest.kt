package tmg.flashback.firebase.converters

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.data.models.stats.Constructor
import tmg.flashback.firebase.models.FSeasonOverviewConstructor
import tmg.testutils.BaseTest

internal class ConstructorConverterTest: BaseTest() {

    @Test
    fun `season overview constructor convert maps fields correctly`() {
        val expected = Constructor(
            id = "id",
            name = "name",
            wikiUrl = "wikiUrl",
            nationality = "nationality",
            nationalityISO = "nationalityISO",
            color = 0 // toColorInt() returns 0 in unit test environment
        )
        val input = FSeasonOverviewConstructor.exampleData()

        assertEquals(expected, input.convert())
    }
}

private fun FSeasonOverviewConstructor.Companion.exampleData(): FSeasonOverviewConstructor {
    return FSeasonOverviewConstructor(
        id = "id",
        name = "name",
        wikiUrl = "wikiUrl",
        nationality = "nationality",
        nationalityISO = "nationalityISO",
        colour = "#0000ff"
    )
}
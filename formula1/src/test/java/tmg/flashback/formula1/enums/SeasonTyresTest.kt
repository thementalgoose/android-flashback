package tmg.flashback.formula1.enums

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.threeten.bp.Year

internal class SeasonTyresTest {

    @Test
    fun `season tyres have value for current year`() {
        val currentYear = Year.now().value

        assertNotNull(SeasonTyres.getBySeason(currentYear))
    }

    @ParameterizedTest(name = "Season tyres value found for {0} season")
    @CsvSource(
        "2011",
        "2012",
        "2013",
        "2014",
        "2015",
        "2016",
        "2017",
        "2018",
        "2019",
        "2020",
        "2021",
        "2022"
    )
    fun `season tyres have value for current year`(season: Int) {
        assertNotNull(SeasonTyres.getBySeason(season))
    }
}
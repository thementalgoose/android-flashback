package tmg.flashback.formula1.enums

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import org.threeten.bp.Year
import tmg.flashback.formula1.R

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

    @ParameterizedTest
    @EnumSource(SeasonTyres::class)
    fun `all dry compound labels are in an order`(tyres: SeasonTyres) {
        val order = mapOf(
            R.string.tyre_hyper_soft to 1,
            R.string.tyre_ultra_soft to 2,
            R.string.tyre_super_soft to 3,
            R.string.tyre_soft to 4,
            R.string.tyre_medium to 5,
            R.string.tyre_hard to 6,
            R.string.tyre_super_hard to 7
        )

        var ref = 0
        tyres.tyres
            .filter { it.tyre.isDry }
            .forEach { list ->
                val orderVal = order[list.label]!!
                if (orderVal <= ref) {
                    assertTrue(false, "Tyre order labels are not in the correct order!")
                }
                ref = orderVal
            }
    }

    @ParameterizedTest
    @EnumSource(SeasonTyres::class)
    fun `all wet compound labels are in an order`(tyres: SeasonTyres) {
        val order = mapOf(
            R.string.tyre_intermediate to 1,
            R.string.tyre_wet to 2
        )

        var ref = 0
        tyres.tyres
            .filter { !it.tyre.isDry }
            .forEach { list ->
                val orderVal = order[list.label]!!
                if (orderVal <= ref) {
                    assertTrue(false, "Tyre order labels are not in the correct order!")
                }
                ref = orderVal
            }
    }
}
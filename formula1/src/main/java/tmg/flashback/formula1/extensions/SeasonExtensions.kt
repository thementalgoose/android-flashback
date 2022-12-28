package tmg.flashback.formula1.extensions

import tmg.flashback.formula1.enums.isStatusFinished
import tmg.flashback.formula1.model.DriverConstructors
import tmg.flashback.formula1.model.Season
import tmg.flashback.formula1.model.SeasonDriverComparison
import tmg.flashback.formula1.model.SeasonDriverComparisonDriver
import kotlin.math.min

fun Season.compareDrivers(driverId1: String, driverId2: String): SeasonDriverComparison? {

    val driver1 = this.drivers.firstOrNull { it.id == driverId1 } ?: return null
    val driver2 = this.drivers.firstOrNull { it.id == driverId2 } ?: return null

    val d1 = ComparisonDTO()
    val d2 = ComparisonDTO()

    this.races.forEach {
        val driver1Overview = it.driverOverview(driverId1)
        val driver2Overview = it.driverOverview(driverId2)

        // Finish head to head
        let(driver1Overview?.race?.finish, driver2Overview?.race?.finish) { val1, val2 ->
            if (val1 < val2) {
                d1.finishes += 1
            } else {
                d2.finishes += 1
            }
        }

        // Qualifying head to head
        let(driver1Overview?.officialQualifyingPosition, driver2Overview?.officialQualifyingPosition) { val1, val2 ->
            if (val1 < val2) {
                d1.qualifying += 1
            } else {
                d2.qualifying += 1
            }
        }

        // Win count
        d1.winCount += if (driver1Overview?.race?.finish == 1) 1 else 0
        d2.winCount += if (driver2Overview?.race?.finish == 1) 1 else 0

        // Podium count
        d1.podiumCount += if (driver1Overview?.race?.finish in 1..3) 1 else 0
        d2.podiumCount += if (driver2Overview?.race?.finish in 1..3) 1 else 0

        // Pole count
        d1.poleCount += if (driver1Overview?.qualified == 1) 1 else 0
        d2.poleCount += if (driver2Overview?.qualified == 1) 1 else 0

        // Best finish position
        d1.bestFinishPosition = min(
            d1.bestFinishPosition,
            driver1Overview?.race?.finish ?: Int.MAX_VALUE
        )
        d2.bestFinishPosition = min(
            d2.bestFinishPosition,
            driver2Overview?.race?.finish ?: Int.MAX_VALUE
        )

        // Best qualifying position
        d1.bestQualifyingPosition = min(
            d1.bestQualifyingPosition ?: Int.MAX_VALUE,
            driver1Overview?.race?.qualified ?: Int.MAX_VALUE
        )
        d2.bestQualifyingPosition = min(
            d2.bestQualifyingPosition ?: Int.MAX_VALUE,
            driver2Overview?.race?.qualified ?: Int.MAX_VALUE
        )

        // DNF count
        d1.dnfCount += if (driver1Overview?.race?.status?.isStatusFinished() != true) 1 else 0
        d2.dnfCount += if (driver2Overview?.race?.status?.isStatusFinished() != true) 1 else 0
    }

    return SeasonDriverComparison(
        season = this.season,
        driver1 = DriverConstructors(
            driver = driver1,
            constructors = getConstructors(driver1)
        ),
        driver1Stats = d1.toModel(),
        driver2 = DriverConstructors(
            driver = driver2,
            constructors = getConstructors(driver2)
        ),
        driver2Stats = d2.toModel()
    )
}


private fun <E, R> let(first: E?, second: R?, result: (E, R) -> Unit) {
    if (first != null && second != null) {
        result(first, second)
    }
}

private data class ComparisonDTO(
    var finishes: Int = 0,
    var qualifying: Int = 0,
    var podiumCount: Int = 0,
    var winCount: Int = 0,
    var poleCount: Int = 0,
    var bestFinishPosition: Int = Int.MAX_VALUE,
    var bestQualifyingPosition: Int? = Int.MAX_VALUE,
    var dnfCount: Int = 0,
) {
    fun toModel() = SeasonDriverComparisonDriver(
        finishes = finishes,
        qualifying = qualifying,
        podiumCount = podiumCount,
        winCount = winCount,
        poleCount = poleCount,
        bestFinishPosition = if (bestFinishPosition == Int.MAX_VALUE) null else bestFinishPosition,
        bestQualifyingPosition = if (bestQualifyingPosition == Int.MAX_VALUE) null else bestQualifyingPosition,
        dnfCount = dnfCount,
    )
}
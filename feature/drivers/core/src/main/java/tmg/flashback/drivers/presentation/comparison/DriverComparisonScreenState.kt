package tmg.flashback.drivers.presentation.comparison

import tmg.flashback.formula1.model.Driver

data class DriverComparisonScreenState(
    val isLoading: Boolean,
    val networkAvailable: Boolean,
    val driverList: List<Driver> = emptyList(),
    val driverLeft: Driver? = null,
    val driverRight: Driver? = null,
    val comparison: Comparison? = null
)

data class Comparison(
    val left: ComparisonValue,
    val right: ComparisonValue,
) {
    fun getPercentages(value: (ComparisonValue) -> Float, highIsBest: Boolean = true): Pair<Float, Float> {
        return getPercentages(
            a = value(left),
            b = value(right),
            highIsBest = highIsBest
        )
    }

    fun getPercentages(a: Float, b: Float, highIsBest: Boolean = true): Pair<Float, Float> {
        if (a == 0f && b == 0f) {
            return 0f to 0f
        }
        return if (highIsBest) {
            when {
                a > b -> 1f to (b / a)
                a == b -> 1f to 1f
                else -> (a / b) to 1f
            }
        } else {
            when {
                a < b -> 1f to (b / a)
                a == b -> 1f to 1f
                else -> (b / a) to 1f
            }
        }
    }
}

data class ComparisonValue(
    val racesHeadToHead: Int,
    val qualifyingHeadToHead: Int,
    val points: Double?,
    val pointsFinishes: Int,
    val podiums: Int,
    val wins: Int,
    val dnfs: Int
)
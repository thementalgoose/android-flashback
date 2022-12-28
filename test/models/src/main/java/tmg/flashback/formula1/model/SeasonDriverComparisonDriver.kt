package tmg.flashback.formula1.model

fun SeasonDriverComparisonDriver.Companion.model(
    finishes: Int = 5,
    qualifying: Int = 6,
    podiumCount: Int = 2,
    winCount: Int = 4,
    poleCount: Int = 1,
    bestFinishPosition: Int? = 8,
    bestQualifyingPosition: Int? = 9,
    dnfCount: Int = 3,
): SeasonDriverComparisonDriver = SeasonDriverComparisonDriver(
    finishes = finishes,
    qualifying = qualifying,
    podiumCount = podiumCount,
    winCount = winCount,
    poleCount = poleCount,
    bestFinishPosition = bestFinishPosition,
    bestQualifyingPosition = bestQualifyingPosition,
    dnfCount = dnfCount,
)
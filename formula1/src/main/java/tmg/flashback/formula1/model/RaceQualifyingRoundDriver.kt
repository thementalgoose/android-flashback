package tmg.flashback.formula1.model

sealed class RaceQualifyingRoundDriver(
    val driver: DriverConstructor,
    val lapTime: LapTime?,
    val position: Int
) {

    class Qualifying(
        driver: DriverConstructor,
        lapTime: LapTime?,
        position: Int
    ): RaceQualifyingRoundDriver(
        driver = driver,
        lapTime = lapTime,
        position = position
    )

    class SprintQualifying(
        driver: DriverConstructor,
        lapTime: LapTime?,
        position: Int,
        val finished: Int,
        val gridPos: Int,
        val points: Double,
        val status: String
    ): RaceQualifyingRoundDriver(
        driver = driver,
        lapTime = lapTime,
        position = position
    )
}
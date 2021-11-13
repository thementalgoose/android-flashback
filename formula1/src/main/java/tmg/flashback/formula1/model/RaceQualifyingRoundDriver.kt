package tmg.flashback.formula1.model

sealed class RaceQualifyingRoundDriver(
    val driver: DriverConstructor,
    val lapTime: LapTime?,
    val position: Int
) {

    data class Qualifying(
        private val _driver: DriverConstructor,
        private val _lapTime: LapTime?,
        private val _position: Int
    ): RaceQualifyingRoundDriver(
        driver = _driver,
        lapTime = _lapTime,
        position = _position
    ) {
        companion object
    }

    data class SprintQualifying(
        private val _driver: DriverConstructor,
        private val _lapTime: LapTime?,
        private val _position: Int,
        val finished: Int,
        val gridPos: Int?,
        val points: Double,
        val status: String
    ): RaceQualifyingRoundDriver(
        driver = _driver,
        lapTime = _lapTime,
        position = _position
    ) {
        companion object
    }
}
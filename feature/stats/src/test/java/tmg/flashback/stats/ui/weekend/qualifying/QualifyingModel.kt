package tmg.flashback.stats.ui.weekend.qualifying

import tmg.flashback.formula1.model.DriverConstructor
import tmg.flashback.formula1.model.LapTime
import tmg.flashback.formula1.model.RaceQualifyingResult
import tmg.flashback.formula1.model.model

fun QualifyingModel.Q1Q2Q3.Companion.model(
    driver: DriverConstructor = DriverConstructor.model(),
    finalQualifyingPosition: Int? = 1,
    q1: RaceQualifyingResult? = RaceQualifyingResult.model(lapTime = LapTime.model(0, 1, 2, 1)),
    q2: RaceQualifyingResult? = RaceQualifyingResult.model(lapTime = LapTime.model(0, 1, 2, 2)),
    q3: RaceQualifyingResult? = RaceQualifyingResult.model(lapTime = LapTime.model(0, 1, 2, 3))
): QualifyingModel.Q1Q2Q3 = QualifyingModel.Q1Q2Q3(
    driver = driver,
    finalQualifyingPosition = finalQualifyingPosition,
    q1 = q1,
    q2 = q2,
    q3 = q3
)

fun QualifyingModel.Q1Q2.Companion.model(
    driver: DriverConstructor = DriverConstructor.model(),
    finalQualifyingPosition: Int? = 1,
    q1: RaceQualifyingResult? = RaceQualifyingResult.model(lapTime = LapTime.model(0, 1, 2, 1)),
    q2: RaceQualifyingResult? = RaceQualifyingResult.model(lapTime = LapTime.model(0, 1, 2, 2))
): QualifyingModel.Q1Q2 = QualifyingModel.Q1Q2(
    driver = driver,
    finalQualifyingPosition = finalQualifyingPosition,
    q1 = q1,
    q2 = q2
)

fun QualifyingModel.Q1.Companion.model(
    driver: DriverConstructor = DriverConstructor.model(),
    finalQualifyingPosition: Int? = 1,
    q1: RaceQualifyingResult? = RaceQualifyingResult.model(lapTime = LapTime.model(0, 1, 2, 1))
): QualifyingModel.Q1 = QualifyingModel.Q1(
    driver = driver,
    finalQualifyingPosition = finalQualifyingPosition,
    q1 = q1
)
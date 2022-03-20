package tmg.flashback.formula1.model

import tmg.flashback.formula1.model.RaceQualifyingType.*

fun Race.Companion.model(
    raceInfo: RaceInfo = RaceInfo.model(),
    qualifying: List<RaceQualifyingRound> = listOf(
        RaceQualifyingRound.model(label = Q1, order = 1, results = listOf(
            RaceQualifyingResult.model(lapTime = LapTime.model(0, 1, 2, 1))
        )),
        RaceQualifyingRound.model(label = Q2, order = 2, results = listOf(
            RaceQualifyingResult.model(lapTime = LapTime.model(0, 1, 2, 2))
        )),
        RaceQualifyingRound.model(label = Q3, order = 3, results = listOf(
            RaceQualifyingResult.model(lapTime = LapTime.model(0, 1, 2, 3))
        ))
    ),
    sprint: List<RaceSprintResult> = listOf(
        RaceSprintResult.model(time = LapTime.model(0, 1, 2, 5))
    ),
    race: List<RaceRaceResult> = listOf(
        RaceRaceResult.model()
    ),
    schedule: List<Schedule> = listOf(
        Schedule.model()
    )
): Race = Race(
    raceInfo = raceInfo,
    qualifying = qualifying,
    sprint = sprint,
    race = race,
    schedule = schedule
)
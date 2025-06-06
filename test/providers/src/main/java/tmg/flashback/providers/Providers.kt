package tmg.flashback.providers

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import java.time.LocalDate
import java.time.LocalTime
import tmg.flashback.formula1.model.CircuitHistoryRace
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.formula1.model.Event
import tmg.flashback.formula1.model.Overview
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.RaceResult
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.formula1.model.SeasonConstructorStandingSeason
import tmg.flashback.formula1.model.SeasonConstructorStandings
import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.formula1.model.SeasonDriverStandings
import tmg.flashback.providers.model.model

class DriverProvider: PreviewParameterProvider<Driver> {
    override val values: Sequence<Driver> = sequenceOf(Driver.model())
}

class ConstructorProvider: PreviewParameterProvider<Constructor> {
    override val values: Sequence<Constructor> = sequenceOf(Constructor.model())
}

class OverviewRaceProvider: PreviewParameterProvider<OverviewRace> {
    override val values: Sequence<OverviewRace> = sequenceOf(OverviewRace.model(
        schedule = listOf(
            Schedule.model(label = "FP1", date = LocalDate.now().minusDays(1), time = LocalTime.of(9, 30)),
            Schedule.model(label = "FP2", date = LocalDate.now().minusDays(1), time = LocalTime.of(13, 0)),
            Schedule.model(label = "FP3", date = LocalDate.now(), time = LocalTime.of(10, 0)),
            Schedule.model(label = "Qualifying", date = LocalDate.now(), time = LocalTime.of(13, 0)),
            Schedule.model(label = "Race", date = LocalDate.now().plusDays(1), time = LocalTime.of(13, 0))
        )
    ))
}

class SeasonConstructorStandingsProvider: PreviewParameterProvider<SeasonConstructorStandings> {
    override val values: Sequence<SeasonConstructorStandings> = sequenceOf(SeasonConstructorStandings.model())
}

class SeasonConstructorStandingSeasonProvider: PreviewParameterProvider<SeasonConstructorStandingSeason> {
    override val values: Sequence<SeasonConstructorStandingSeason> = sequenceOf(SeasonConstructorStandingSeason.model())
}

class SeasonDriverStandingsProvider: PreviewParameterProvider<SeasonDriverStandings> {
    override val values: Sequence<SeasonDriverStandings> = sequenceOf(SeasonDriverStandings.model())
}

class SeasonDriverStandingSeasonProvider: PreviewParameterProvider<SeasonDriverStandingSeason> {
    override val values: Sequence<SeasonDriverStandingSeason> = sequenceOf(SeasonDriverStandingSeason.model())
}

class ScheduleProvider: PreviewParameterProvider<Schedule> {
    override val values: Sequence<Schedule> = sequenceOf(Schedule.model())
}

class ScheduleListProvider: PreviewParameterProvider<List<Schedule>> {
    override val values: Sequence<List<Schedule>> = sequenceOf(listOf(
        Schedule.model(
            label = "FP1",
            date = LocalDate.of(2022, 1, 1),
            time = LocalTime.of(10, 0)
        ),
        Schedule.model(
            label = "FP2",
            date = LocalDate.of(2022, 1, 1),
            time = LocalTime.of(14, 0)
        ),
        Schedule.model(
            label = "FP3",
            date = LocalDate.of(2022, 1, 2),
            time = LocalTime.of(11, 0)
        ),
        Schedule.model(
            label = "Qualifying",
            date = LocalDate.of(2022, 1, 2),
            time = LocalTime.of(15, 0)
        ),
        Schedule.model(
            label = "Race",
            date = LocalDate.of(2022, 1, 3),
            time = LocalTime.of(15, 0)
        )
    ))
}

class OverviewProvider: PreviewParameterProvider<Overview> {
    override val values: Sequence<Overview> = sequenceOf(Overview.model())
}

class RaceProvider: PreviewParameterProvider<Race> {
    override val values: Sequence<Race> = sequenceOf(Race.model())
}

class DriverConstructorProvider: PreviewParameterProvider<DriverEntry> {
    override val values: Sequence<DriverEntry> = sequenceOf(DriverEntry.model())
}

class RaceRaceResultProvider: PreviewParameterProvider<RaceResult> {
    override val values: Sequence<RaceResult> = sequenceOf(RaceResult.model())
}

class CircuitHistoryRaceProvider: PreviewParameterProvider<CircuitHistoryRace> {
    override val values: Sequence<CircuitHistoryRace> = sequenceOf(CircuitHistoryRace.model())
}

class EventProvider: PreviewParameterProvider<Event> {
    override val values: Sequence<Event> = sequenceOf(Event.model())
}
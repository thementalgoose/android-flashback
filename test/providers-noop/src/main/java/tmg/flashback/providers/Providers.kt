package tmg.flashback.providers

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import tmg.flashback.formula1.model.CircuitHistoryRace
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.formula1.model.Event
import tmg.flashback.formula1.model.Overview
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.RaceResult
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.formula1.model.SeasonConstructorStandingSeason
import tmg.flashback.formula1.model.SeasonConstructorStandings
import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.formula1.model.SeasonDriverStandings

class DriverProvider: PreviewParameterProvider<Driver> {
    override val values: Sequence<Driver> = sequenceOf()
}

class ConstructorProvider: PreviewParameterProvider<Constructor> {
    override val values: Sequence<Constructor> = sequenceOf()
}

class OverviewRaceProvider: PreviewParameterProvider<OverviewRaceProvider> {
    override val values: Sequence<OverviewRaceProvider> = sequenceOf()
}

class SeasonConstructorStandingsProvider: PreviewParameterProvider<SeasonConstructorStandings> {
    override val values: Sequence<SeasonConstructorStandings> = sequenceOf()
}

class SeasonConstructorStandingSeasonProvider: PreviewParameterProvider<SeasonConstructorStandingSeason> {
    override val values: Sequence<SeasonConstructorStandingSeason> = sequenceOf()
}

class SeasonDriverStandingsProvider: PreviewParameterProvider<SeasonDriverStandings> {
    override val values: Sequence<SeasonDriverStandings> = sequenceOf()
}

class SeasonDriverStandingSeasonProvider: PreviewParameterProvider<SeasonDriverStandingSeason> {
    override val values: Sequence<SeasonDriverStandingSeason> = sequenceOf()
}

class ScheduleProvider: PreviewParameterProvider<Schedule> {
    override val values: Sequence<Schedule> = sequenceOf()
}

class ScheduleListProvider: PreviewParameterProvider<List<Schedule>> {
    override val values: Sequence<List<Schedule>> = sequenceOf()
}

class OverviewProvider: PreviewParameterProvider<Overview> {
    override val values: Sequence<Overview> = sequenceOf()
}

class RaceProvider: PreviewParameterProvider<Race> {
    override val values: Sequence<Race> = sequenceOf()
}

class DriverConstructorProvider: PreviewParameterProvider<DriverEntry> {
    override val values: Sequence<DriverEntry> = sequenceOf()
}

class RaceRaceResultProvider: PreviewParameterProvider<RaceResult> {
    override val values: Sequence<RaceResult> = sequenceOf()
}

class CircuitHistoryRaceProvider: PreviewParameterProvider<CircuitHistoryRace> {
    override val values: Sequence<CircuitHistoryRace> = sequenceOf()
}

class EventProvider: PreviewParameterProvider<Event> {
    override val values: Sequence<Event> = sequenceOf()
}
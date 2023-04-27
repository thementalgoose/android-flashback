package tmg.flashback.statistics.room

import androidx.room.Database
import androidx.room.RoomDatabase
import tmg.flashback.statistics.room.dao.CircuitDao
import tmg.flashback.statistics.room.dao.ConstructorDao
import tmg.flashback.statistics.room.dao.DriverDao
import tmg.flashback.statistics.room.dao.EventsDao
import tmg.flashback.statistics.room.dao.OverviewDao
import tmg.flashback.statistics.room.dao.ScheduleDao
import tmg.flashback.statistics.room.dao.SeasonDao
import tmg.flashback.statistics.room.dao.SeasonStandingsDao
import tmg.flashback.statistics.room.models.circuit.Circuit
import tmg.flashback.statistics.room.models.circuit.CircuitRound
import tmg.flashback.statistics.room.models.circuit.CircuitRoundResult
import tmg.flashback.statistics.room.models.constructors.Constructor
import tmg.flashback.statistics.room.models.constructors.ConstructorSeason
import tmg.flashback.statistics.room.models.constructors.ConstructorSeasonDriver
import tmg.flashback.statistics.room.models.drivers.Driver
import tmg.flashback.statistics.room.models.drivers.DriverSeason
import tmg.flashback.statistics.room.models.drivers.DriverSeasonRace
import tmg.flashback.statistics.room.models.overview.Event
import tmg.flashback.statistics.room.models.overview.Overview
import tmg.flashback.statistics.room.models.overview.Schedule
import tmg.flashback.statistics.room.models.race.QualifyingResult
import tmg.flashback.statistics.room.models.race.RaceInfo
import tmg.flashback.statistics.room.models.race.RaceResult
import tmg.flashback.statistics.room.models.race.SprintQualifyingResult
import tmg.flashback.statistics.room.models.race.SprintRaceResult
import tmg.flashback.statistics.room.models.standings.ConstructorStanding
import tmg.flashback.statistics.room.models.standings.ConstructorStandingDriver
import tmg.flashback.statistics.room.models.standings.DriverStanding
import tmg.flashback.statistics.room.models.standings.DriverStandingConstructor

@Database(
    version = 9,
    entities = [
        Circuit::class,
        CircuitRound::class,
        CircuitRoundResult::class,
        Constructor::class,
        ConstructorSeason::class,
        ConstructorSeasonDriver::class,
        Driver::class,
        DriverSeason::class,
        DriverSeasonRace::class,
        Overview::class,
        Schedule::class,
        RaceInfo::class,
        RaceResult::class,
        SprintRaceResult::class,
        SprintQualifyingResult::class,
        QualifyingResult::class,
        DriverStanding::class,
        DriverStandingConstructor::class,
        ConstructorStanding::class,
        ConstructorStandingDriver::class,
        Event::class
    ]
)
abstract class FlashbackDatabase: RoomDatabase() {
    abstract fun overviewDao(): OverviewDao
    abstract fun circuitDao(): CircuitDao
    abstract fun constructorDao(): ConstructorDao
    abstract fun driverDao(): DriverDao
    abstract fun seasonDao(): SeasonDao
    abstract fun seasonStandingDao(): SeasonStandingsDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun eventsDao(): EventsDao
}
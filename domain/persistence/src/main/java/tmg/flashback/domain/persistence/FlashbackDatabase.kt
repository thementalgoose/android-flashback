package tmg.flashback.domain.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import tmg.flashback.domain.persistence.dao.CircuitDao
import tmg.flashback.domain.persistence.dao.ConstructorDao
import tmg.flashback.domain.persistence.dao.DriverDao
import tmg.flashback.domain.persistence.dao.EventsDao
import tmg.flashback.domain.persistence.dao.OverviewDao
import tmg.flashback.domain.persistence.dao.ScheduleDao
import tmg.flashback.domain.persistence.dao.SeasonDao
import tmg.flashback.domain.persistence.dao.SeasonStandingsDao
import tmg.flashback.domain.persistence.models.circuit.Circuit
import tmg.flashback.domain.persistence.models.circuit.CircuitRound
import tmg.flashback.domain.persistence.models.circuit.CircuitRoundResult
import tmg.flashback.domain.persistence.models.constructors.Constructor
import tmg.flashback.domain.persistence.models.constructors.ConstructorSeason
import tmg.flashback.domain.persistence.models.constructors.ConstructorSeasonDriver
import tmg.flashback.domain.persistence.models.drivers.Driver
import tmg.flashback.domain.persistence.models.drivers.DriverSeason
import tmg.flashback.domain.persistence.models.drivers.DriverSeasonRace
import tmg.flashback.domain.persistence.models.overview.Event
import tmg.flashback.domain.persistence.models.overview.Overview
import tmg.flashback.domain.persistence.models.overview.Schedule
import tmg.flashback.domain.persistence.models.race.QualifyingResult
import tmg.flashback.domain.persistence.models.race.RaceInfo
import tmg.flashback.domain.persistence.models.race.RaceResult
import tmg.flashback.domain.persistence.models.race.SprintQualifyingResult
import tmg.flashback.domain.persistence.models.race.SprintRaceResult
import tmg.flashback.domain.persistence.models.standings.ConstructorStanding
import tmg.flashback.domain.persistence.models.standings.ConstructorStandingDriver
import tmg.flashback.domain.persistence.models.standings.DriverStanding
import tmg.flashback.domain.persistence.models.standings.DriverStandingConstructor

@Database(
    version = 11,
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
    ],
    exportSchema = false
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
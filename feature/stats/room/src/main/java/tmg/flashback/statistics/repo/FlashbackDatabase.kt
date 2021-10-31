package tmg.flashback.statistics.repo

import androidx.room.Database
import androidx.room.RoomDatabase
import tmg.flashback.statistics.repo.dao.*
import tmg.flashback.statistics.repo.models.circuit.Circuit
import tmg.flashback.statistics.repo.models.circuit.CircuitRound
import tmg.flashback.statistics.repo.models.circuit.CircuitRoundResult
import tmg.flashback.statistics.repo.models.constructors.*
import tmg.flashback.statistics.repo.models.drivers.Driver
import tmg.flashback.statistics.repo.models.drivers.DriverSeason
import tmg.flashback.statistics.repo.models.drivers.DriverSeasonRace
import tmg.flashback.statistics.repo.models.overview.Overview
import tmg.flashback.statistics.repo.models.round.*

@Database(
    version = 1,
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
        Round::class,
        RaceResult::class,
        QualifyingResult::class
    ]
)
abstract class FlashbackDatabase: RoomDatabase() {
    abstract fun overviewDao(): OverviewDao
    abstract fun circuitDao(): CircuitDao
    abstract fun constructorDao(): ConstructorDao
    abstract fun driverDao(): DriverDao
    abstract fun seasonDao(): SeasonDao
}
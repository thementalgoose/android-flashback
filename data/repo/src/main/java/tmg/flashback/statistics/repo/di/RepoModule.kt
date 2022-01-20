package tmg.flashback.statistics.repo.di

import org.koin.dsl.module
import tmg.flashback.statistics.network.di.networkModule
import tmg.flashback.statistics.repo.*
import tmg.flashback.statistics.repo.mappers.app.*
import tmg.flashback.statistics.repo.mappers.network.*
import tmg.flashback.statistics.repo.repository.CacheRepository
import tmg.flashback.statistics.repo.repository.RepoCacheRepository
import tmg.flashback.statistics.room.di.roomModule

val repoModule = networkModule + roomModule + module {

    // Mappers - App
    single { CircuitMapper() }
    single { ConstructorDataMapper() }
    single { ConstructorMapper(get(), get()) }
    single { ConstructorStandingMapper(get(), get()) }
    single { DriverDataMapper() }
    single { DriverMapper(get(), get(), get()) }
    single { DriverStandingMapper(get(), get()) }
    single { OverviewMapper(get()) }
    single { RaceMapper(get(), get(), get(), get()) }
    single { SeasonMapper(get(), get()) }
    single { ScheduleMapper() }
    single { WinterTestingMapper() }

    // Mappers - Network
    single { NetworkCircuitDataMapper() }
    single { NetworkCircuitMapper(get()) }
    single { NetworkConstructorDataMapper() }
    single { NetworkConstructorMapper() }
    single { NetworkConstructorStandingMapper() }
    single { NetworkDriverDataMapper() }
    single { NetworkDriverMapper() }
    single { NetworkDriverStandingMapper() }
    single { NetworkOverviewMapper() }
    single { NetworkRaceDataMapper() }
    single { NetworkRaceMapper() }
    single { NetworkScheduleMapper() }

    // Cache tracking
    single<CacheRepository> { RepoCacheRepository(get()) }

    // Repositories
    single { CircuitRepository(get(), get(), get(), get(), get(), get()) }
    single { ConstructorRepository(get(), get(), get(), get(), get(), get(), get(), get()) }
    single { DriverRepository(get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    single { OverviewRepository(get(), get(), get(), get(), get(), get(), get()) }
    single { RaceRepository(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    single { SeasonRepository(get(), get(), get(), get(), get()) }
    single { ScheduleRepository(get(), get(), get(), get()) }
}
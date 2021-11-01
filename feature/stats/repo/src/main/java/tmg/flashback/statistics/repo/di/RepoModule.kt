package tmg.flashback.statistics.repo.di

import org.koin.dsl.module
import tmg.flashback.statistics.network.di.networkModule
import tmg.flashback.statistics.repo.CircuitRepository
import tmg.flashback.statistics.repo.ConstructorRepository
import tmg.flashback.statistics.repo.DriverRepository
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.statistics.repo.mappers.app.*
import tmg.flashback.statistics.repo.mappers.network.*
import tmg.flashback.statistics.room.di.roomModule

val repoModule = networkModule + roomModule + module {

    // Mappers - App
    single { CircuitMapper() }
    single { ConstructorDataMapper() }
    single { ConstructorMapper() }
    single { DriverDataMapper() }
    single { DriverMapper(get(), get()) }
    single { OverviewMapper() }

    // Mappers - Network
    single { NetworkCircuitDataMapper() }
    single { NetworkCircuitMapper(get()) }
    single { NetworkConstructorDataMapper() }
    single { NetworkConstructorMapper() }
    single { NetworkDriverDataMapper() }
    single { NetworkDriverMapper() }
    single { NetworkOverviewMapper() }
    single { NetworkRaceDataMapper() }

    // Repositories
    single { CircuitRepository(get(), get(), get(), get(), get(), get()) }
    single { ConstructorRepository(get(), get(), get(), get(), get()) }
    single { DriverRepository(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    single { OverviewRepository(get(), get(), get(), get(), get()) }
}
package tmg.flashback.statistics.repo.di

import org.koin.dsl.module
import tmg.flashback.statistics.repo.CircuitRepository
import tmg.flashback.statistics.repo.DriverRepository
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.statistics.repo.mappers.app.CircuitMapper
import tmg.flashback.statistics.repo.mappers.app.DriverMapper
import tmg.flashback.statistics.repo.mappers.app.OverviewMapper
import tmg.flashback.statistics.repo.mappers.network.NetworkCircuitMapper
import tmg.flashback.statistics.repo.mappers.network.NetworkOverviewMapper

val repoModule = module {

    // Mappers
    single { CircuitMapper() }
    single { DriverMapper() }
    single { OverviewMapper() }
    single { NetworkCircuitMapper() }
    single { NetworkOverviewMapper() }

    // Repositories
    single { CircuitRepository(get(), get(), get(), get(), get()) }
    single { DriverRepository(get(), get(), get(), get(), get()) }
    single { OverviewRepository(get(), get(), get(), get(), get()) }
}
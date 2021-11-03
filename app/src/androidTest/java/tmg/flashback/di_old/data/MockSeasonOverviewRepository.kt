package tmg.flashback.di_old.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tmg.flashback.data.db.stats.SeasonOverviewRepository
import tmg.flashback.formula1.model.*
import tmg.flashback.formula1.model.Constructor

internal object MockSeasonOverviewRepository: SeasonOverviewRepository {

    override fun getCircuits(season: Int): Flow<List<CircuitSummary>> = flow {
        emit(listOf(mockCircuitLeicester, mockCircuitNottingham))
    }

    override fun getCircuit(season: Int, round: Int): Flow<CircuitSummary?> = flow {
        when (round) {
            1 -> emit(MOCK_RACE_1.circuit)
            2 -> emit(MOCK_RACE_2.circuit)
            else -> emit(null)
        }
    }

    override fun getConstructor(season: Int, constructorId: String): Flow<Constructor?> = flow {
        when (constructorId) {
            mockConstructorGreen.id -> emit(mockConstructorGreen)
            mockConstructorBlue.id -> emit(mockConstructorBlue)
            else -> emit(null)
        }
    }

    override fun getDriver(season: Int, driver: String): Flow<DriverWithEmbeddedConstructor?> = flow {
        when (driver) {
            MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_ALEX.id -> emit(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_ALEX)
            MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_BRIAN.id -> emit(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_BRIAN)
            MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_CHARLIE.id -> emit(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_CHARLIE)
            MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL.id -> emit(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL)
            else -> emit(null)
        }
    }

    override fun getAllConstructors(season: Int): Flow<List<Constructor>> = flow {
        emit(mockAllConstructors)
    }

    override fun getSeasonOverview(season: Int): Flow<Season> = flow {
        emit(mockSeason)
    }

    override fun getSeasonRound(season: Int, round: Int): Flow<Race?> = flow {
        when (round) {
            1 -> emit(MOCK_RACE_1)
            2 -> emit(MOCK_RACE_2)
            else -> emit(null)
        }
    }
}
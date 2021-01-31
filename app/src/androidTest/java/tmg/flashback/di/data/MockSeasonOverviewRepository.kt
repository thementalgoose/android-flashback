package tmg.flashback.di.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tmg.flashback.data.db.stats.SeasonOverviewRepository
import tmg.flashback.data.models.stats.*

internal object MockSeasonOverviewRepository: SeasonOverviewRepository {

    override fun getCircuits(season: Int): Flow<List<CircuitSummary>> = flow {
        emit(listOf(mockCircuitLeicester, mockCircuitNottingham))
    }

    override fun getCircuit(season: Int, round: Int): Flow<CircuitSummary?> = flow {
        when (round) {
            1 -> emit(mockRound1.circuit)
            2 -> emit(mockRound2.circuit)
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

    override fun getDriver(season: Int, driver: String): Flow<Driver?> = flow {
        when (driver) {
            mockDriverAlex.id -> emit(mockDriverAlex)
            mockDriverBrian.id -> emit(mockDriverBrian)
            mockDriverCharlie.id -> emit(mockDriverCharlie)
            mockDriverDaniel.id -> emit(mockDriverDaniel)
            else -> emit(null)
        }
    }

    override fun getAllConstructors(season: Int): Flow<List<Constructor>> = flow {
        emit(mockAllConstructors)
    }

    override fun getSeasonOverview(season: Int): Flow<Season> = flow {
        emit(mockSeason)
    }

    override fun getSeasonRound(season: Int, round: Int): Flow<Round?> = flow {
        when (round) {
            1 -> emit(mockRound1)
            2 -> emit(mockRound2)
            else -> emit(null)
        }
    }
}
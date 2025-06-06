package tmg.flashback.data.persistence.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tmg.flashback.data.persistence.models.circuit.Circuit
import tmg.flashback.data.persistence.models.circuit.CircuitHistory
import tmg.flashback.data.persistence.models.circuit.CircuitRound
import tmg.flashback.data.persistence.models.circuit.CircuitRoundResult


@Dao
interface CircuitDao {

    @Query("SELECT * FROM circuit WHERE id == :id LIMIT 1")
    fun getCircuit(id: String): Flow<Circuit?>

    @Transaction
    @Query("SELECT * FROM circuit WHERE id == :id")
    fun getCircuitHistory(id: String): Flow<CircuitHistory>

    @Query("SELECT COUNT(*) FROM CircuitRound WHERE circuit_id == :id")
    suspend fun getCircuitRounds(id: String): Int

    @Query("SELECT * FROM Circuit")
    fun getCircuits(): Flow<List<Circuit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCircuit(circuit: List<Circuit>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCircuitRounds(rounds: List<CircuitRound>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCircuitRoundResults(results: List<CircuitRoundResult>)
}
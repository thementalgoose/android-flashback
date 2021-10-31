package tmg.flashback.statistics.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tmg.flashback.statistics.room.models.circuit.Circuit
import tmg.flashback.statistics.room.models.circuit.CircuitHistory
import tmg.flashback.statistics.room.models.circuit.CircuitRound
import tmg.flashback.statistics.room.models.circuit.CircuitRoundResult


@Dao
interface CircuitDao {

    @Query("SELECT * FROM circuit WHERE id == :id LIMIT 1")
    fun getCircuit(id: String): Flow<Circuit?>

    @Transaction
    @Query("SELECT * FROM circuit WHERE id == :id")
    fun getCircuitHistory(id: String): Flow<CircuitHistory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCircuit(circuit: List<Circuit>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCircuitRounds(rounds: List<CircuitRound>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCircuitRoundResults(results: List<CircuitRoundResult>)
}
package tmg.flashback.statistics.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import tmg.flashback.formula1.model.CircuitHistory
import tmg.flashback.statistics.room.models.circuit.Circuit

private typealias RoomCircuitHistory = tmg.flashback.statistics.room.models.circuit.CircuitHistory

@Dao
interface CircuitDao {

    @Query("SELECT * FROM circuit WHERE id == :id LIMIT 1")
    fun getCircuit(id: String): Circuit?

    @Query("SELECT * FROM circuit WHERE id == :id LIMIT 1")
    fun observeCircuit(id: String): Flow<Circuit?>

    @Query("SELECT * FROM circuitround WHERE circuit_id == :id")
    fun getCircuitHistory(id: String): Flow<CircuitHistory>

    @Insert
    fun insertCircuit(circuit: List<Circuit>)

    @Transaction
    @Insert
    fun insertCircuitHistory(circuit: RoomCircuitHistory)
}
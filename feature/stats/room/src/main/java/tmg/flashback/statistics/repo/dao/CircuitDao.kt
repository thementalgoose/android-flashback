package tmg.flashback.statistics.repo.dao

import androidx.room.Dao
import androidx.room.Query
import tmg.flashback.statistics.repo.models.circuit.Circuit

@Dao
interface CircuitDao {

    @Query("SELECT * FROM circuit WHERE id == :id LIMIT 1")
    fun getCircuit(id: String): Circuit?
}
package tmg.flashback.statistics.repo.models.round

import androidx.room.*

data class RoundData(
    @Embedded
    val round: Round,
    @Relation(
        entity = QualifyingResult::class,
        parentColumn = "id",
        entityColumn = "qualifying_result_id"
    )
    val qualifying: List<QualifyingDriverResult>,
    @Relation(
        entity = RaceResult::class,
        parentColumn = "id",
        entityColumn = "race_result_id"
    )
    val race: List<RaceDriverResult>
)
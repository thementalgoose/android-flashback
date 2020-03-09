package tmg.f1stats.repo_firebase.models

import tmg.f1stats.repo.enums.SyncProgress
import tmg.f1stats.repo.enums.SyncType
import tmg.f1stats.repo.models.Sync

data class FSync(
    val type: String,
    val progress: String,
    val errorMsg: String?,
    val season: Int,
    val round: Int?,
    val completedAt: String?
)

fun FSync.toModel(): Sync {
    return Sync(
        type = SyncType.values().first { it.type == this.type },
        progress = SyncProgress.values().first { it.type == this.progress },
        errorMsg = this.errorMsg,
        season = this.season,
        round = this.round,
        completedAt = this.completedAt
    )
}

fun Sync.toModel(): FSync {
    return FSync(
        type = this.type.type,
        progress = this.progress.type,
        errorMsg = this.errorMsg,
        season = this.season,
        round = this.round,
        completedAt = this.completedAt
    )
}
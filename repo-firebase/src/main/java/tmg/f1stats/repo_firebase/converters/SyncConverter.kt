package tmg.f1stats.repo_firebase.converters

import tmg.f1stats.repo.enums.SyncProgress
import tmg.f1stats.repo.enums.SyncType
import tmg.f1stats.repo.models.Sync
import tmg.f1stats.repo_firebase.models.FSync

fun FSync.toModel(documentId: String): Sync {
    return Sync(
        id = documentId,
        type = SyncType.values().firstOrNull { it.type == this.type } ?: SyncType.SEASON_OVERVIEW,
        progress = SyncProgress.values().firstOrNull { it.type == this.progress } ?: SyncProgress.NEW,
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
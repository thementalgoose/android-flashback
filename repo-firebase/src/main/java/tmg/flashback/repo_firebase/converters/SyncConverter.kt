package tmg.flashback.repo_firebase.converters

import tmg.flashback.repo.enums.SyncProgress
import tmg.flashback.repo.enums.SyncType
import tmg.flashback.repo.models.Sync
import tmg.flashback.repo_firebase.models.FSync

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
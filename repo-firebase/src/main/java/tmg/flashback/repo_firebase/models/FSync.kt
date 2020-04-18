package tmg.flashback.repo_firebase.models

data class FSync(
    val type: String,
    val progress: String,
    val errorMsg: String?,
    val season: Int,
    val round: Int?,
    val completedAt: String?
) {
    constructor() : this("", "", null, -1, -1, null)
}
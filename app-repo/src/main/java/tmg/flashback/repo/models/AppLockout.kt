package tmg.flashback.repo.models

data class AppLockout(
    val show: Boolean,
    val title: String,
    val message: String,
    val linkText: String?,
    val link: String?,
    val version: Int?
)
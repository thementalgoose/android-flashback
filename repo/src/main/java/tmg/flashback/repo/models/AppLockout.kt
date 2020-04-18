package tmg.flashback.repo.models

data class AppLockout(
    val show: Boolean,
    val message: String,
    val linkText: String?,
    val link: String?
)
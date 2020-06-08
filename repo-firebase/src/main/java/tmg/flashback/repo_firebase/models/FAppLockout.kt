package tmg.flashback.repo_firebase.models

data class FAppLockout(
    val show: Boolean? = false,
    val title: String? = null,
    val message: String? = null,
    val linkText: String? = null,
    val link: String? = null
)
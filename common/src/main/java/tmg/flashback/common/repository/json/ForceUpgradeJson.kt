package tmg.flashback.common.repository.json

import kotlinx.serialization.Serializable

@Serializable
data class ForceUpgradeJson(
    val title: String? = null,
    val message: String? = null,
    val link: String? = null,
    val linkText: String? = null
)
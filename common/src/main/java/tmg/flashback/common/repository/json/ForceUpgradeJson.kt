package tmg.flashback.common.repository.json

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class ForceUpgradeJson(
    val title: String? = null,
    val message: String? = null,
    val link: String? = null,
    val linkText: String? = null
)
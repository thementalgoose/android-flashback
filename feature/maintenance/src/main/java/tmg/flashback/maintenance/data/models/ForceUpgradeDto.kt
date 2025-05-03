package tmg.flashback.maintenance.data.models

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
internal data class ForceUpgradeDto(
    val title: String? = null,
    val message: String? = null,
    val link: String? = null,
    val linkText: String? = null
)
package tmg.flashback.flashbacknews.api.models.news

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class News(
    val message: String,
    val url: String? = null,
    val image: String? = null,
    val dateAdded: String
)
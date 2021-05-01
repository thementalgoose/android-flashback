package tmg.configuration.repository.json

import tmg.configuration.repository.models.ForceUpgrade

//region Converters

data class ForceUpgradeJson(
    val title: String? = null,
    val message: String? = null,
    val link: String? = null,
    val linkText: String? = null
)

fun ForceUpgradeJson.convert(): ForceUpgrade? {
    if (title.isNullOrEmpty() || message.isNullOrEmpty()) {
        return null
    }
    return ForceUpgrade(
        title = this.title,
        message = this.message,
        link = when {
            link.isNullOrEmpty() || linkText.isNullOrEmpty() -> null
            else -> Pair(linkText, link)
        }
    )
}

//endregion
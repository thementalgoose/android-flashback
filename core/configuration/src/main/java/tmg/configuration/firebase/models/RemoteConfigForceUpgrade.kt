package tmg.configuration.firebase.models

import tmg.configuration.repository.models.ForceUpgrade

data class RemoteConfigForceUpgrade(
        val title: String? = null,
        val message: String? = null,
        val link: String? = null,
        val linkText: String? = null
)

//region Converters

fun RemoteConfigForceUpgrade.convert(): ForceUpgrade? {
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
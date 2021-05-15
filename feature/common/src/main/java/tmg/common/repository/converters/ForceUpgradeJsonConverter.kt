package tmg.common.repository.converters

import tmg.common.repository.json.ForceUpgradeJson
import tmg.common.repository.model.ForceUpgrade

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
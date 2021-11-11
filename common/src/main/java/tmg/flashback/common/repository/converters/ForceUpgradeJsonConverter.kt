package tmg.flashback.common.repository.converters

import tmg.flashback.common.repository.json.ForceUpgradeJson
import tmg.flashback.common.repository.model.ForceUpgrade

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
package tmg.flashback.forceupgrade.repository.converters

import tmg.flashback.forceupgrade.repository.json.ForceUpgradeJson
import tmg.flashback.forceupgrade.repository.model.ForceUpgrade


internal fun ForceUpgradeJson.convert(): ForceUpgrade? {
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
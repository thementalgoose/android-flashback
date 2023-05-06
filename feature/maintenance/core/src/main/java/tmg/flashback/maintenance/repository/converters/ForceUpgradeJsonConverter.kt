package tmg.flashback.maintenance.repository.converters

import tmg.flashback.maintenance.repository.json.ForceUpgradeJson
import tmg.flashback.maintenance.repository.model.ForceUpgrade


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
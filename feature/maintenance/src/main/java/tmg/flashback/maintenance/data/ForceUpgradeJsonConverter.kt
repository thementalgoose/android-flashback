package tmg.flashback.maintenance.data

import tmg.flashback.maintenance.data.models.ForceUpgradeDto
import tmg.flashback.maintenance.repository.model.ForceUpgrade


internal fun ForceUpgradeDto.convert(): ForceUpgrade? {
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
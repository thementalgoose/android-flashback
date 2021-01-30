package tmg.flashback.firebase.converters

import tmg.flashback.data.models.remoteconfig.AppLockout
import tmg.flashback.firebase.models.FAppLockout

fun FAppLockout.convert(): AppLockout {
    return AppLockout(
        show = show ?: false,
        message = message ?: "",
        linkText = linkText,
        link = link,
        title = title ?: "App lockout",
        version = when {
            version == null -> null
            version <= 0 -> null
            else -> version
        }
    )
}
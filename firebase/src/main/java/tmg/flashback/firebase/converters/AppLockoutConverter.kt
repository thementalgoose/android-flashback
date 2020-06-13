package tmg.flashback.firebase.converters

import tmg.flashback.repo.models.AppLockout
import tmg.flashback.firebase.models.FAppLockout

fun FAppLockout.convert(): AppLockout {
    return AppLockout(
        show = show ?: false,
        message = message ?: "",
        linkText = linkText,
        link = link,
        title = title ?: "App lockout"
    )
}
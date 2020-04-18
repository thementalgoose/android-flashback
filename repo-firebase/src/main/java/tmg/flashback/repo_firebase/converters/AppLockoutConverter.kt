package tmg.flashback.repo_firebase.converters

import tmg.flashback.repo.models.AppLockout
import tmg.flashback.repo_firebase.models.FAppLockout

fun FAppLockout.convert(): AppLockout {
    return AppLockout(
        show = show ?: false,
        message = message ?: "",
        linkText = linkText,
        link = link
    )
}
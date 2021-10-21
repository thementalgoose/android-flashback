package tmg.flashback.firebase.mappers

import tmg.flashback.data.models.AppLockout
import tmg.flashback.firebase.models.FAppLockout

class AppLockoutMapper {

    fun mapAppLockout(input: FAppLockout): AppLockout {
        return AppLockout(
            show = input.show ?: false,
            message = input.message ?: "",
            linkText = input.linkText,
            link = input.link,
            title = input.title ?: "App lockout",
            version = when {
                input.version == null -> null
                input.version <= 0 -> null
                else -> input.version
            }
        )
    }
}
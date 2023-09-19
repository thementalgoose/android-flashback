package tmg.flashback.ui.permissions

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.device.AppPermissions
import tmg.flashback.ui.R

val AppPermissions.RuntimePermission.description: Int
    @StringRes
    get() = when (this) {
        AppPermissions.RuntimeNotifications -> R.string.permissions_rationale_runtime_notifications_description
    }

val AppPermissions.RuntimePermission.icon: Int
    @DrawableRes
    get() = when (this) {
        AppPermissions.RuntimeNotifications -> R.drawable.ic_permission_notifications
    }
package tmg.flashback.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

sealed class AppPermissions: Parcelable {

    @Parcelize
    sealed class RuntimePermission(
        val permission: String,
        @StringRes
        val description: Int,
        @DrawableRes
        val icon: Int
    ): AppPermissions() {
        companion object {
            fun get(permissionStrings: List<String>): List<RuntimePermission> {
                return permissionStrings.mapNotNull {
                    when (it) {
                        RuntimeNotifications.permission -> RuntimeNotifications
                        else -> null
                    }
                }
            }
        }
    }

    // https://developer.android.com/training/permissions/requesting-special
    @Parcelize
    sealed class SpecialPermission(
        val getIntent: () -> Intent,
    ): AppPermissions()


    @Parcelize
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    data object RuntimeNotifications: RuntimePermission(
        permission = Manifest.permission.POST_NOTIFICATIONS,
        description = R.string.permissions_rationale_runtime_notifications_description,
        icon = R.drawable.ic_permission_notifications
    )

    @Parcelize
    @RequiresApi(Build.VERSION_CODES.S)
    data object ScheduleExactAlarms: SpecialPermission(
        getIntent = { Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM) }
    )
}
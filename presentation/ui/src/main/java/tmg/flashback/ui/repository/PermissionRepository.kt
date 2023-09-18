package tmg.flashback.ui.repository

import android.app.AlarmManager
import android.content.Context.ALARM_SERVICE
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import tmg.flashback.navigation.ActivityProvider
import tmg.flashback.ui.AppPermissions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionRepository @Inject constructor(
    private val topActivityProvider: ActivityProvider
) {

    fun isEnabled(permission: AppPermissions.RuntimePermission): Boolean {
        return when (permission) {
            AppPermissions.RuntimeNotifications -> isRuntimeNotificationsEnabled
        }
    }

    fun isEnabled(permission: AppPermissions.SpecialPermission): Boolean {
        return when (permission) {
            AppPermissions.ScheduleExactAlarms -> isExactAlarmEnabled
        }
    }

    val isRuntimeNotificationsEnabled: Boolean
        get() {
            val baseActivity = topActivityProvider.activity ?: return false
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                when (ContextCompat.checkSelfPermission(baseActivity, AppPermissions.RuntimeNotifications.permission)) {
                    PackageManager.PERMISSION_GRANTED -> true
                    else -> false
                }
            } else {
                true
            }
        }

    val isExactAlarmEnabled: Boolean
        get() {
            val alarmManager = topActivityProvider.activity?.getSystemService(ALARM_SERVICE) as? AlarmManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                alarmManager?.canScheduleExactAlarms() ?: false
            } else {
                true
            }
        }
}
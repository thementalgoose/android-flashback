package tmg.flashback.ui.repository

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import tmg.flashback.navigation.ActivityProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionRepository @Inject constructor(
    private val topActivityProvider: tmg.flashback.navigation.ActivityProvider
) {
    val isRuntimeNotificationsEnabled: Boolean
        get() {
            val baseActivity = topActivityProvider.activity ?: return false
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                when (ContextCompat.checkSelfPermission(baseActivity, Manifest.permission.POST_NOTIFICATIONS)) {
                    PackageManager.PERMISSION_GRANTED -> true
                    else -> false
                }
            } else {
                true
            }
        }
}
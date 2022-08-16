package tmg.flashback.device.managers

import javax.inject.Inject
import javax.inject.Singleton

interface PermissionManager {
    suspend fun isPermissionGranted(permission: String): Boolean
}
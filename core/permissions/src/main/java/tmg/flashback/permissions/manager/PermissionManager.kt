package tmg.flashback.permissions.manager

import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import tmg.flashback.ui.navigation.ActivityProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionManager @Inject constructor(
    private val topActivityProvider: ActivityProvider
) {
//    suspend fun isPermissionGranted(permission: String): Boolean {
//
//    }

    fun isPermissionGranted(permission: String, info: String) = flow<Boolean> {
        val flowCollector = this
        val activity = topActivityProvider.activity ?: throw java.lang.IllegalStateException("Trying to request permission when no activity is present")

        when {
            ContextCompat.checkSelfPermission(activity, permission) == PERMISSION_GRANTED -> {
                emit(true)
            }
            shouldShowRequestPermissionRationale(activity, permission) -> {

            }
            else -> {
                val requestPermissionLauncher = (activity as AppCompatActivity)
                    .registerForActivityResult(RequestPermission()) { isGranted ->
                        runBlocking { this@flow.emit(isGranted) }
                    }
                requestPermissionLauncher.launch(permission)
            }
        }
    }
}
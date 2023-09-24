package tmg.flashback.ui.managers

import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import kotlinx.coroutines.CompletableDeferred
import tmg.flashback.navigation.ActivityProvider
import tmg.flashback.device.AppPermissions
import tmg.flashback.ui.R
import tmg.flashback.ui.base.BaseActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionManager @Inject constructor(
    private val topActivityProvider: ActivityProvider
) {
    private var completableDeferred: CompletableDeferred<Map<String, Boolean>>? = null

    private val baseActivity: BaseActivity?
        get() = topActivityProvider.activity as? BaseActivity

    internal val activityContract = ActivityResultCallback<Map<String, Boolean>> { isGranted ->
        completableDeferred?.complete(isGranted)
        completableDeferred = null
        val numberGranted = isGranted.count { it.value }
        if (numberGranted != isGranted.size) {
            val numberDenied = isGranted.size - numberGranted
            when (val string = baseActivity?.resources?.getQuantityString(R.plurals.permissions_rationale_permissions_denied, numberDenied, numberDenied)) {
                null -> Toast.makeText(baseActivity, R.string.permissions_rationale_permission_denied, Toast.LENGTH_LONG).show()
                else -> Toast.makeText(baseActivity, string, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun requestPermission(vararg rationaleType: AppPermissions.RuntimePermission): CompletableDeferred<Map<String, Boolean>> {
        completableDeferred = CompletableDeferred()
        baseActivity?.requestPermission(*rationaleType)
        return completableDeferred ?: CompletableDeferred<Map<String, Boolean>>().apply {
            this.complete(rationaleType.associate { it.permission to false })
        }
    }
}
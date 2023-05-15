package tmg.flashback.ui.managers

import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import kotlinx.coroutines.CompletableDeferred
import tmg.flashback.ui.R
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.navigation.ActivityProvider
import tmg.flashback.ui.permissions.RationaleType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionManager @Inject constructor(
    private val topActivityProvider: ActivityProvider
) {
    private var completableDeferred: CompletableDeferred<Boolean>? = null

    private val baseActivity: BaseActivity?
        get() = topActivityProvider.activity as? BaseActivity

    internal val activityContract = ActivityResultCallback<Boolean> { isGranted ->
        completableDeferred?.complete(isGranted)
        completableDeferred = null
        if (!isGranted) {
            Toast.makeText(
                baseActivity,
                R.string.permissions_rationale_permission_denied,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun requestPermission(rationaleType: RationaleType): CompletableDeferred<Boolean> {
        completableDeferred = CompletableDeferred()
        baseActivity?.requestPermission(rationaleType)
        return completableDeferred ?: CompletableDeferred<Boolean>().apply {
            this.complete(false)
        }
    }
}